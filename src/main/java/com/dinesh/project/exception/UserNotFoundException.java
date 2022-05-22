package com.dinesh.project.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException() {
        super("User is Not Found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
