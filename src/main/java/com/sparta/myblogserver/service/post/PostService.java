package com.sparta.myblogserver.service.post;

import com.sparta.myblogserver.entity.domain.dto.post.PostReq;
import com.sparta.myblogserver.entity.domain.dto.post.PostRes;
import java.util.List;

public interface PostService {

    public Long createPost(PostReq postReq);

    public List<PostRes> findAllPosts();

    public Long updatePost(Long id, PostReq postReq);

    public Long deletePost(Long id,String password);

    public PostRes findPostById(Long id);
}
