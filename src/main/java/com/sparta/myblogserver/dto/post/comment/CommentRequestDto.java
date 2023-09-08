package com.sparta.myblogserver.dto.post.comment;

import com.sparta.myblogserver.entity.post.comment.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용을 입력해 주세요.")
    private String content;

    private String username;

    public Comment toEntity() {
        return Comment.builder()
                .content(this.getContent())
                .username(this.getUsername())
                .build();
    }

    public void addUsername(String username) {
        this.username = username;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
