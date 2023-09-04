package com.sparta.myblogserver.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}