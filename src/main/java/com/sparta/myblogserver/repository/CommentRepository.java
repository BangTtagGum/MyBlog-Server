package com.sparta.myblogserver.repository;

import com.sparta.myblogserver.entity.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
