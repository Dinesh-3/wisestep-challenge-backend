package com.dinesh.project.service;

import com.dinesh.project.dto.Email;
import com.dinesh.project.dto.Login;
import com.dinesh.project.exception.ClientErrorException;
import com.dinesh.project.exception.ResourceNotFoundException;
import com.dinesh.project.exception.UserNotFoundException;
import com.dinesh.project.model.Auth;
import com.dinesh.project.model.User;
import com.dinesh.project.repository.AuthRepository;
import com.dinesh.project.repository.UserRepository;
import com.dinesh.project.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final AuthRepository authRepository;
    private final UserRepository repository;
    private final NotificationService notificationService;

    private final SpringTemplateEngine templateEngine;
    private final String subject = "One Time Password";

    @Value("${otp.resend.interval}")
    private int RESEND_INTERVAL_SEC;

    @Autowired
    public UserService(AuthRepository authRepository, UserRepository repository, NotificationService notificationService, SpringTemplateEngine templateEngine) {
        this.authRepository = authRepository;
        this.repository = repository;
        this.notificationService = notificationService;
        this.templateEngine = templateEngine;
    }

    public ResponseData signup(User user) {
        Optional<User> optionalUser = repository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()) throw new ClientErrorException("Email Already Exist!");

        repository.save(user);
        return ResponseData.ok();
    }

    public ResponseData login(Login loginRequest, Map<String, String> headers) {
        System.out.println("headers = " + headers);

        User user = repository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User Not Found for %s ", loginRequest.getEmail())));

        Auth auth = authRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Token Not Sent"));

        handleExistingSession(auth);

        LocalDateTime expiryTime = auth.getTokenExpiryTime();
        if(expiryTime.isBefore(LocalDateTime.now())) throw new ClientErrorException("OTP Token expired try again");

        boolean isEqual = auth.getOtpToken().equalsIgnoreCase(loginRequest.getOtp());

        if(!isEqual) throw new ClientErrorException("Invalid OTP");

        auth.setLoggedIn(true);
        auth.setSessionExpiryTime(LocalDateTime.now().plusDays(1));
        auth.setSessionToken(UUID.randomUUID().toString());

        authRepository.save(auth);

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("session", auth.getSessionToken());

        return ResponseData.ok(map);
    }

    public ResponseData logout(Map<String, String> headers) {
        String token = headers.get("session-token");
        if(token == null || token.isEmpty()) throw new ClientErrorException("Token is required");

        Auth auth = authRepository.findBySessionToken(token).orElseThrow(() -> new ClientErrorException("Invalid Token, token not found"));

        auth.setLoggedIn(false);
        auth.setSessionExpiryTime(LocalDateTime.now());

        authRepository.save(auth);

        return new ResponseData(true, "Logout Success");
    }

    private void handleExistingSession(Auth auth) {

        LocalDateTime sessionExpiryTime = auth.getSessionExpiryTime();
        if(sessionExpiryTime == null) return;

        boolean isBefore = sessionExpiryTime.isBefore(LocalDateTime.now());
        if(isBefore) auth.setLoggedIn(false);

        if(auth.isLoggedIn()) throw new ClientErrorException("User already logged in, Logout and try again");
    }

    public ResponseData getUserById(Map<String, String> headers) {
        String token = headers.get("session-token");
        if(token == null) throw new ClientErrorException("Token is required to get user info");

        Auth auth = authRepository.findBySessionToken(token).orElseThrow(() -> new ClientErrorException("Invalid Token"));

        if(!auth.isLoggedIn() || auth.getSessionExpiryTime().isBefore(LocalDateTime.now())){
            auth.setLoggedIn(false);
            authRepository.save(auth);
            throw new ClientErrorException("Token Expired Login Again");
        }

        User user = repository.findById(auth.getUserId()).orElseThrow(() -> new UserNotFoundException("User with id " + auth.getUserId() + " not Found"));
        return ResponseData.ok(user);
    }

    public ResponseData sendOTP(Map<String, String> request) {

        System.out.println("request = " + request);
        String userEmail = request.get("email");

        if(userEmail == null || userEmail.isEmpty()) throw new ClientErrorException("Email is required");

        userEmail = userEmail.trim();

        String finalUserEmail = userEmail;
        User user = repository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(String.format("User Not Found for %s ", finalUserEmail)));

        Optional<Auth> optionalAuth = authRepository.findByUserId(user.getId());
        optionalAuth.ifPresent(this::validateExistingToken);

        Context context = new Context();
        String otp = getOTP();
        context.setVariable("otp",otp);
        context.setVariable("user", user);
        String body = templateEngine.process("otpEmail", context);

        Email email = new Email(user.getEmail(), subject, body);
        notificationService.sendEmail(email);

        Auth auth = optionalAuth.orElse(new Auth(user.getId()));
        auth.setOtpToken(otp);
        auth.setTokenExpiryTime(LocalDateTime.now().plusMinutes(RESEND_INTERVAL_SEC));

        authRepository.save(auth);

        return new ResponseData(true, "OTP sent to email");
    }

    private String getOTP() {
        int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
        return String.valueOf(randomNum);
    }

    private void validateExistingToken(Auth auth) {
        LocalDateTime expiryTime = auth.getTokenExpiryTime();
        if(expiryTime == null) return;
        boolean after = expiryTime.isAfter(LocalDateTime.now());
        if(after) throw new ClientErrorException("Token not expired. Please use existing token");
    }

}
