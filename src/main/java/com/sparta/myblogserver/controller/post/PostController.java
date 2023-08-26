package com.sparta.myblogserver.controller.post;

import com.sparta.myblogserver.entity.domain.dto.post.PostReq;
import com.sparta.myblogserver.entity.domain.dto.post.PostRes;
import com.sparta.myblogserver.service.post.PostService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostRes> findAllPosts() {
        return postService.findAllPosts();
    }

    @GetMapping("/{id}")
    public PostRes findPostById(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @PostMapping
    public Long createPost(@RequestBody PostReq postReq) {
        return postService.createPost(postReq);
    }

    @PutMapping("/{id}")
    public Long updatePost(@PathVariable Long id, @RequestBody PostReq postReq) {
        return postService.updatePost(id, postReq);
    }

    @DeleteMapping("/{id}")
    public Long deletePost(@PathVariable Long id, @RequestBody Map<String, String> password) {
        return postService.deletePost(id, password.get("password"));
    }
}
