package com.sparta.myblogserver.domain.post.dto;

import lombok.Getter;

@Getter
public class PostReq {
    String title;
    String content;
    String author;
    String password;
}
