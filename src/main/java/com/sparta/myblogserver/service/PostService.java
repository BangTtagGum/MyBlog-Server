package com.sparta.myblogserver.service;

import static com.sparta.myblogserver.entity.user.UserRoleEnum.ADMIN;

import com.sparta.myblogserver.dto.post.PostRequestDto;
import com.sparta.myblogserver.dto.post.PostResponseDto;
import com.sparta.myblogserver.entity.post.Post;
import com.sparta.myblogserver.entity.user.User;
import com.sparta.myblogserver.repository.CommentRepository;
import com.sparta.myblogserver.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        // DB 저장
        postRequestDto.addAuthor(user.getUsername());
        Post post = postRequestDto.toEntity();
        Post savedpost = postRepository.save(post);
        return savedpost.toRes();
    }

    public List<PostResponseDto> findAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto findPostById(Long id) {
        Post findPost = findPost(id);
        return findPost.toRes();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post findPost = findPost(id);

        // 수정하려는 user의 권한이 ADMIN 이거나 user가 게시글의 작성자 일 경우.
        if (user.getRole().equals(ADMIN) || findPost.getAuthor().equals(user.getUsername())) {
            findPost.update(postRequestDto);
            return findPost.toRes();
        }
        throw new IllegalArgumentException("작성자만 수정이 가능합니다.");
    }

    @Transactional
    public Long deletePost(Long id, User user) {
        Post findPost = findPost(id);

        // 삭제하려는 user의 권한이 ADMIN 이거나 user가 게시글의 작성자 일 경우.
        if (user.getRole().equals(ADMIN) || findPost.getAuthor().equals(user.getUsername())) {
            commentRepository.deleteAll(findPost.getCommentList());
            postRepository.delete(findPost);
            return id;
        }
        throw new IllegalArgumentException("작성자만 삭제가 가능합니다.");
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("해당 id의 게시물이 존재하지 않습니다. Post ID: " + id);
        });
    }
}
