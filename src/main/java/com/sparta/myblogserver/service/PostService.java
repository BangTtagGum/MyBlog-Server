package com.sparta.myblogserver.service;

import com.sparta.myblogserver.dto.post.PostRequestDto;
import com.sparta.myblogserver.dto.post.PostResponseDto;
import com.sparta.myblogserver.entity.post.Post;
import com.sparta.myblogserver.repository.PostRepository;
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
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto findPostById(Long id) {
        Post findPost = findPost(id);
        return findPost.toRes();

    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, String username) {
        Post findPost = findPost(id);
        if (!findPost.getAuthor().equals(username)) {
            throw new RuntimeException("작성자만 수정이 가능합니다.");
        }
        findPost.update(postRequestDto);
        return findPost.toRes();
    }

    @Transactional
    public Long deletePost(Long id, String username) throws RuntimeException {

        Post findPost = findPost(id);
        if (!findPost.getAuthor().equals(username)) {
            throw new RuntimeException("작성자만 삭제가 가능합니다.");
        }
        postRepository.delete(findPost);
        return id;
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
