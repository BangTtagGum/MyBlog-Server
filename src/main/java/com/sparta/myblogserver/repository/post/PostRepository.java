package com.sparta.myblogserver.repository.post;

import com.sparta.myblogserver.entity.domain.dto.post.PostReq;
import com.sparta.myblogserver.entity.domain.dto.post.PostRes;
import com.sparta.myblogserver.entity.domain.entity.Post;
import java.util.List;

public interface PostRepository {

    public Long save(Post post);

    public List<PostRes> findAll();

    public Long update(Long id, PostReq postReq);

    public Long delete(Long id);

    public Post findById(Long id);

}
