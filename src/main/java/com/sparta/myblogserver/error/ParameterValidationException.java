package com.sparta.myblogserver.error;

public class ParameterValidationException extends RuntimeException{

    public ParameterValidationException(String message) {
        super(message);
    }
}
