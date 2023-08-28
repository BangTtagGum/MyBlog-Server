package com.sparta.myblogserver.domain.post.dto;

import com.sparta.myblogserver.domain.post.entity.Post;
import lombok.Getter;

@Getter
public class PostReq {

    String title;
    String content;
    String author;
    String password;

    public Post toEntity() {
        return Post.builder()
                .title(this.getTitle())
                .content(this.getContent())
                .author(this.getAuthor())
                .password(this.getPassword())
                .build();
    }

}
