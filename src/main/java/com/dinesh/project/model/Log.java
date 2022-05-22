package com.dinesh.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("log")
public class Log {
    @Id
    private String id;

    private LocalDateTime createdAt;
    private String message;
    private String stackTrace;

    public Log() {
    }

    public Log(String message, String stackTrace) {
        this.createdAt = LocalDateTime.now();
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }
}
