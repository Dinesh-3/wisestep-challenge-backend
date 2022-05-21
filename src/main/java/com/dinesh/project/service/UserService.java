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
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class UserService {

    private final AuthRepository authRepository;
    private final UserRepository repository;
    private final NotificationService notificationService;

    private final SpringTemplateEngine templateEngine;
    private final String subject = "One Time Password";

    @Autowired
    public UserService(AuthRepository authRepository, UserRepository repository, NotificationService notificationService, SpringTemplateEngine templateEngine) {
        this.authRepository = authRepository;
        this.repository = repository;
        this.notificationService = notificationService;
        this.templateEngine = templateEngine;
    }

    public ResponseData signup(User user) {
        repository.save(user);
        return ResponseData.ok();
    }

    public ResponseData login(Login loginRequest) {

        User user = repository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new UserNotFoundException(String.format("User Not Found for %s ", loginRequest.getEmail())));

        Auth auth = authRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Token Not Sent"));

        LocalDateTime expiryTime = auth.getExpiryTime();
        if(expiryTime.isBefore(LocalDateTime.now())) throw new ClientErrorException("Token expired try again");

        boolean isEqual = auth.getToken().equalsIgnoreCase(loginRequest.getOtp());

        if(!isEqual) throw new ClientErrorException("Invalid OTP");

        return ResponseData.ok(user);
    }

    public ResponseData getUserById(String userId) {
        User user = repository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not Found"));
        return ResponseData.ok(user);
    }

    public ResponseData sendOTP(Map<String, String> request) {

        System.out.println("request = " + request);
        String userEmail = request.get("email");

        if(userEmail == null || userEmail.isBlank()) throw new ClientErrorException("Email is required");

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
        auth.setToken(otp);
        auth.setExpiryTime(LocalDateTime.now().plusMinutes(3));

        authRepository.save(auth);

        return new ResponseData(true, "OTP sent to email");
    }

    private String getOTP() {
        int randomNum = ThreadLocalRandom.current().nextInt(10000, 100000);
        return String.valueOf(randomNum);
    }

    private void validateExistingToken(Auth auth) {
        LocalDateTime expiryTime = auth.getExpiryTime();
        boolean after = expiryTime.isAfter(LocalDateTime.now());
        if(after) throw new ClientErrorException("Token not expired. Please use existing token");
    }

}
