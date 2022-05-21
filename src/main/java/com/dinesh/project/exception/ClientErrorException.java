package com.dinesh.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ClientErrorException extends HttpClientErrorException {

    public ClientErrorException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
