package com.sparta.myblogserver.domain.post.entity;

import com.sparta.myblogserver.domain.base.JDBCBaseEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Post extends JDBCBaseEntity {

    Long id;
    String title;
    String content;
    String author;
    String password;

}
