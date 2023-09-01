package com.sparta.myblogserver.domain.dto.post;

import com.sparta.myblogserver.domain.entity.post.Post;
import lombok.Getter;

@Getter
public class PostRequestDto {

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
