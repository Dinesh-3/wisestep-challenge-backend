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

    private LocalDateTime expiryTime;

    public Auth() {
    }

    public Auth(String userId, String token, LocalDateTime expiryTime) {
        this.userId = userId;
        this.token = token;
        this.expiryTime = expiryTime;
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

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public String toString() {
        return "{\"Auth\":{"
                + "\"id\":\"" + id + "\""
                + ", \"userId\":\"" + userId + "\""
                + ", \"token\":\"" + token + "\""
                + ", \"expiryTime\":" + expiryTime
                + "}}";
    }
}
