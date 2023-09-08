package com.sparta.myblogserver.controller.response;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends BaseResponse {

    public ErrorResponse(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
