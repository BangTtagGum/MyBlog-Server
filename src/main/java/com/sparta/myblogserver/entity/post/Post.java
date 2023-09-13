package com.sparta.myblogserver.entity.post;

import com.sparta.myblogserver.dto.post.PostRequestDto;
import com.sparta.myblogserver.dto.post.PostResponseDto;
import com.sparta.myblogserver.entity.post.comment.Comment;
import com.sparta.myblogserver.entity.timestamp.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
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
public class Post extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    // 연관관계 주입
    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Likes> likeList = new ArrayList<>();

    public Post(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.author = requestDto.getAuthor();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public PostResponseDto toRes() {
        return PostResponseDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .author(this.getAuthor())
                .commentList(this.getCommentList().stream()
                        .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                        .collect(Collectors.toList())) // 작성 날짜 내림차순으로 정렬
                .createdAt(this.getCreatedAt())
                .modifiedAt(this.getModifiedAt())
                .build();
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        comment.setPost(this);
    }
}
