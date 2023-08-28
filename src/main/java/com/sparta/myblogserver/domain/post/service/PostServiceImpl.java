package com.sparta.myblogserver.domain.post.service;

import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import com.sparta.myblogserver.domain.post.entity.Post;
import com.sparta.myblogserver.domain.post.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    @Transactional
    public PostRes createPost(PostReq postReq) {
        // DB 저장
        Post post = postReq.toEntity();
        Post savedpost = postRepository.save(post);
        return savedpost.toRes();
    }

    @Override
    public List<PostRes> findAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostRes::new).toList();
    }

    @Override
    public PostRes findPostById(Long id) {
        Post findPost = findPost(id);
        return findPost.toRes();

    }

    @Override
    @Transactional
    public PostRes updatePost(Long id, PostReq postReq) {
        Post findPost = findPost(id);
        if (passwordValidationCheck(findPost.getPassword(), postReq.getPassword())) {
            findPost.update(postReq);
            return findPost.toRes();
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
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
