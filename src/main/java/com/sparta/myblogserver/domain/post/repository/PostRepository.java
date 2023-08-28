package com.sparta.myblogserver.domain.post.repository;

import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import com.sparta.myblogserver.domain.post.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

}
