package com.sparta.myblogserver.repository;

import com.sparta.myblogserver.entity.post.Likes;
import com.sparta.myblogserver.entity.post.Post;
import com.sparta.myblogserver.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByPostAndUser(Post post, User user);
}