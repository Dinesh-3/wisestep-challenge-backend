package com.dinesh.project.exception;

import com.dinesh.project.util.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class CustomExceptionHandler {
    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ResponseEntity<ResponseData> handleValidationExceptions( Exception ex) {
        ResponseData responseBody = new ResponseData(false, ex.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ResponseData> handleUnexpectedTypeException( Exception ex) {
        ResponseData responseBody = new ResponseData(false, ex.getMessage(), null);
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MissingRequestValueException.class, HttpMessageNotReadableException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseData> handleClientException(Exception ex) {
        ResponseData responseBody = new ResponseData(false, ex.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ResponseData> handleException(HttpClientErrorException ex) {
        ex.printStackTrace();
        ResponseData responseBody = new ResponseData(false, ex.getStatusText());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData> handleException(Exception ex) {
        ex.printStackTrace();
        ResponseData responseBody = new ResponseData(false, ex.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
