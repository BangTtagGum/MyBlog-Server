package com.sparta.myblogserver.controller.message;

import org.springframework.http.HttpStatus;

public class FailureMessage extends Message{

    public FailureMessage(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
