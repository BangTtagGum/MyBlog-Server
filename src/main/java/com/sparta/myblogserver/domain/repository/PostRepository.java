package com.sparta.myblogserver.domain.repository;

import com.sparta.myblogserver.domain.entity.post.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

}
