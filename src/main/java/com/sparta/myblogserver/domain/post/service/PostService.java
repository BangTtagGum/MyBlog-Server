package com.sparta.myblogserver.domain.post.service;

import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import java.util.List;

public interface PostService {

    public Long createPost(PostReq postReq);

    public List<PostRes> findAllPosts();

    public Long updatePost(Long id, PostReq postReq);

    public Long deletePost(Long id,String password);

    public PostRes findPostById(Long id);
}
