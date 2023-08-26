package com.sparta.myblogserver.domain.post.dto;

import java.time.LocalDate;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRes {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Date createdAt;
    private Date modifiedAt;

}
