package com.sparta.myblogserver.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank
    @Size(min = 4, max = 10, message = "사용자 이름은 4자 이상 10자 이하입니다.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 15, message = "비밀번호는 8자 이상 15자 이하입니다.")
    private String password;
}