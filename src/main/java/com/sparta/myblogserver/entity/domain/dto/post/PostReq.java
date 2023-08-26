package com.sparta.myblogserver.entity.domain.dto.post;

import lombok.Getter;

@Getter
public class PostReq {
    String title;
    String content;
    String author;
    String password;
}
