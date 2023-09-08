package com.sparta.myblogserver.entity.post.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.myblogserver.dto.post.comment.CommentRequestDto;
import com.sparta.myblogserver.dto.post.comment.CommentResponseDto;
import com.sparta.myblogserver.entity.post.Post;
import com.sparta.myblogserver.entity.timestamp.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

    // 다대일 연관관계 주입 (외래키의 주인)
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private Post post;

    public CommentResponseDto toRes() {
        return CommentResponseDto.builder()
                .id(this.getId())
                .content(this.getContent())
                .username(this.getUsername())
                .createdAt(this.getCreatedAt())
                .modifiedAt(this.getModifiedAt())
                .build();
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
