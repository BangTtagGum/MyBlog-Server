package com.sparta.myblogserver.domain.post.entity;

import com.sparta.myblogserver.domain.base.Timestamped;
import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Table
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String content;
    String author;
    String password;

    public void update(PostReq postReq) {
        this.title = postReq.getTitle();
        this.content = postReq.getContent();
        this.author = postReq.getAuthor();
    }

    public PostRes toRes() {
        return PostRes.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .author(this.getAuthor())
                .createdAt(this.getCreatedAt())
                .modifiedAt(this.getModifiedAt())
                .build();
    }
}
