package com.sparta.myblogserver.entity.domain.entity;

import com.sparta.myblogserver.entity.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Post extends BaseEntity {

    Long id;
    String title;
    String content;
    String author;
    String password;

}
