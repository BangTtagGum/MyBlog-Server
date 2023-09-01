package com.sparta.myblogserver.domain.service;

import com.sparta.myblogserver.domain.dto.post.PostRequestDto;
import com.sparta.myblogserver.domain.dto.post.PostResponseDto;
import com.sparta.myblogserver.domain.entity.post.Post;
import com.sparta.myblogserver.domain.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl {

    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        // DB 저장
        Post post = postRequestDto.toEntity();
        Post savedpost = postRepository.save(post);
        return savedpost.toRes();
    }

    public List<PostResponseDto> findAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    public PostResponseDto findPostById(Long id) {
        Post findPost = findPost(id);
        return findPost.toRes();

    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto) {
        Post findPost = findPost(id);
        if (passwordValidationCheck(findPost.getPassword(), postRequestDto.getPassword())) {
            findPost.update(postRequestDto);
            return findPost.toRes();
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public Long deletePost(Long id, String password) {
        Post findPost = findPost(id);
        if (passwordValidationCheck(findPost.getPassword(), password)) {
            postRepository.delete(findPost);
            return id;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("게시물이 존재하지 않습니다.");
        });
    }

    private Boolean passwordValidationCheck(String expect, String actual) {
        if (expect.equals(actual)) {
            return true;
        }
        return false;
    }
}
