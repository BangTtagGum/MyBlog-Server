package com.sparta.myblogserver.domain.post.service;

import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import com.sparta.myblogserver.global.common.exception.PasswordMissMatchException;
import java.util.List;

public interface PostService {

    public PostRes createPost(PostReq postReq);

    public List<PostRes> findAllPosts();

    public PostRes updatePost(Long id, PostReq postReq) throws PasswordMissMatchException;

    public Long deletePost(Long id, String password) throws PasswordMissMatchException;

    public PostRes findPostById(Long id);
}
