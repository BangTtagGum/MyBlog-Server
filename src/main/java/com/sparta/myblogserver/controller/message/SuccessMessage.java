package com.sparta.myblogserver.controller.message;

import org.springframework.http.HttpStatus;

public class SuccessMessage extends Message {

    public SuccessMessage(String message) {
        super(HttpStatus.OK, message);
    }

    public SuccessMessage(String message, Object data) {
        super(HttpStatus.OK, message, data);
    }
}
