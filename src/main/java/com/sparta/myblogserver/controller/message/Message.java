package com.sparta.myblogserver.controller.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class Message {

    private HttpStatus httpStatus;
    private String message;

}
