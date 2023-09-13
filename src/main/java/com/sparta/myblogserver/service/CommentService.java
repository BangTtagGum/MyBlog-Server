package com.sparta.myblogserver.service;

import static com.sparta.myblogserver.entity.user.UserRoleEnum.*;

import com.sparta.myblogserver.dto.post.comment.CommentRequestDto;
import com.sparta.myblogserver.dto.post.comment.CommentResponseDto;
import com.sparta.myblogserver.entity.post.Post;
import com.sparta.myblogserver.entity.post.Comment;
import com.sparta.myblogserver.entity.user.User;
import com.sparta.myblogserver.repository.CommentRepository;
import com.sparta.myblogserver.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post findPost = postRepository.findById(postId).orElseThrow(
                () -> {
                    throw new IllegalArgumentException("해당 id의 게시물이 존재하지 않습니다. Post ID: " + postId);
                }
        );

        // 댓글 생성
        requestDto.addUsername(user.getUsername());
        Comment comment = new Comment(requestDto);
        findPost.addComment(comment);

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user) {
        Comment findComment = findComment(id);

        // 유저의 권한이 ADMIN 이거나 댓글 작성한 user와 수정하려는 user가 같은 경우
        if (user.getRole().equals(ADMIN) || user.getUsername().equals(findComment.getUsername())) {
            findComment.update(requestDto);
            return new CommentResponseDto(findComment);
        }
        throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
    }

    @Transactional
    public Long deleteComment(Long id, User user) {
        Comment findComment = findComment(id);

        // 유저의 권한이 ADMIN 이거나 댓글 작성한 user와 삭제하려는 user가 같은 경우
        if (user.getRole().equals(ADMIN) || user.getUsername().equals(findComment.getUsername())) {
            commentRepository.delete(findComment);
            return id;
        }
        throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
    }

    private Comment findComment(Long id) {
        Comment findComment = commentRepository.findById(id).orElseThrow(
                () -> {
                    throw new IllegalArgumentException(("해당 id의 댓글이 존재하지 않습니다. Comment ID: " + id));
                }
        );
        return findComment;
    }
}
