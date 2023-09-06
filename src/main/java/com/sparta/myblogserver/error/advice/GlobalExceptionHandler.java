package com.sparta.myblogserver.error.advice;

import com.sparta.myblogserver.controller.message.FailureMessage;
import com.sparta.myblogserver.error.ParameterValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ParameterValidationException.class)
    public ResponseEntity<FailureMessage> parameterValidationExHandler(
            ParameterValidationException e) {
        return ResponseEntity.badRequest()
                .body(new FailureMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailureMessage> illegalArgumentExHandler(
            IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                .body(new FailureMessage(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<FailureMessage> usernameNotFoundExHandler(
            UsernameNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(new FailureMessage(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<FailureMessage> runtimeExHandler(RuntimeException e) {
        return ResponseEntity.internalServerError()
                .body(new FailureMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailureMessage> exHandler(
            Exception e) {
        return ResponseEntity.internalServerError()
                .body(new FailureMessage(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
