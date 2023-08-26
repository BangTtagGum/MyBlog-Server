package com.sparta.myblogserver.domain.post.repository;

import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import com.sparta.myblogserver.domain.post.entity.Post;
import java.util.List;

public interface PostRepository {

    public Long save(Post post);

    public List<PostRes> findAll();

    public Long update(Long id, PostReq postReq);

    public Long delete(Long id);

    public Post findById(Long id);

}
