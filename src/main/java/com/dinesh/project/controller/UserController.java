package com.dinesh.project.controller;

import com.dinesh.project.dto.Login;
import com.dinesh.project.model.User;
import com.dinesh.project.service.UserService;
import com.dinesh.project.util.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    ResponseEntity<ResponseData> signup(@RequestBody User user){
        ResponseData result = userService.signup(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    ResponseEntity<ResponseData> login(@RequestBody Login loginRequest){
        ResponseData result = userService.login(loginRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<ResponseData> getUserById(@PathVariable("id") String userId){
        ResponseData result = userService.getUserById(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/email")
    ResponseEntity<ResponseData> sendOTP(@RequestBody Map<String, String> request){
        ResponseData result = userService.sendOTP(request);

        return ResponseEntity.ok(result);
    }

}
