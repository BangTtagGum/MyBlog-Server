package com.sparta.myblogserver.service.post;

import com.sparta.myblogserver.entity.domain.dto.post.PostReq;
import com.sparta.myblogserver.entity.domain.dto.post.PostRes;
import com.sparta.myblogserver.entity.domain.entity.Post;
import com.sparta.myblogserver.repository.post.PostRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public Long createPost(PostReq postReq) {
        // RequestDto -> Entity
        Post post = Post.builder()
                .title(postReq.getTitle())
                .content(postReq.getContent())
                .author(postReq.getAuthor())
                .password(postReq.getPassword())
                .build();

        // DB 저장
        Long savedPostId = postRepository.save(post);

        return savedPostId;
    }

    @Override
    public List<PostRes> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public PostRes findPostById(Long id) {
        Post findPost = postRepository.findById(id);
        if (findPost != null) {
            return PostRes.builder()
                    .id(findPost.getId())
                    .title(findPost.getTitle())
                    .content(findPost.getContent())
                    .author(findPost.getAuthor())
                    .createdAt(findPost.getCreatedAt())
                    .modifiedAt(findPost.getModifiedAt())
                    .build();
        } else {

            throw new IllegalArgumentException("해당 게시글은 존재하지 않습니다.");
        }
    }

    @Override
    public Long updatePost(Long id, PostReq postReq) {
        Post findPost = postRepository.findById(id);
        if (findPost != null) {
            if (postReq.getPassword().equals(findPost.getPassword())) {
                return postRepository.update(id, postReq);
            } else {
                throw new IllegalArgumentException("게시글 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 게시글은 존재하지 않습니다.");
        }

    }

    @Override
    public Long deletePost(Long id,String password) {
        Post findPost = postRepository.findById(id);
        if (findPost != null) {
            if (password.equals(findPost.getPassword())) {
                return postRepository.delete(id);
            } else {
                throw new IllegalArgumentException("게시글 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 게시글은 존재하지 않습니다.");
        }
    }
}
