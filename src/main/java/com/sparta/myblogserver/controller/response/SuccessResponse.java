package com.sparta.myblogserver.controller.response;

import org.springframework.http.HttpStatus;

public class SuccessResponse extends BaseResponse {

    public SuccessResponse(String message) {
        super(HttpStatus.OK, message);
    }

    public SuccessResponse(String message, Object data) {
        super(HttpStatus.OK, message, data);
    }
}
