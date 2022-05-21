package com.dinesh.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("auth")
public class Auth {
    @Id
    private String id;
    private String userId;
    private String token;
    private LocalDateTime tokenExpiryTime;
    private boolean isLoggedIn;
    private LocalDateTime sessionExpiryTime;
    private String sessionToken;

    public Auth() {
    }

    public Auth(String userId, String token, LocalDateTime tokenExpiryTime) {
        this.userId = userId;
        this.token = token;
        this.tokenExpiryTime = tokenExpiryTime;
    }

    public Auth(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getTokenExpiryTime() {
        return tokenExpiryTime;
    }

    public void setTokenExpiryTime(LocalDateTime tokenExpiryTime) {
        this.tokenExpiryTime = tokenExpiryTime;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public LocalDateTime getSessionExpiryTime() {
        return sessionExpiryTime;
    }

    public void setSessionExpiryTime(LocalDateTime sessionExpiryTime) {
        this.sessionExpiryTime = sessionExpiryTime;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
