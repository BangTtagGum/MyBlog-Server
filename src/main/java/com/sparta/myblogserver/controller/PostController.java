package com.sparta.myblogserver.controller;


import com.sparta.myblogserver.dto.post.PostRequestDto;
import com.sparta.myblogserver.dto.post.PostResponseDto;
import com.sparta.myblogserver.service.PostServiceImpl;
import java.util.HashMap;
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

    private final PostServiceImpl postServiceImpl;

    @GetMapping
    public List<PostResponseDto> findAllPosts() {
        return postServiceImpl.findAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponseDto findPostById(@PathVariable Long id) {
        return postServiceImpl.findPostById(id);
    }

    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto) {
            return postServiceImpl.createPost(postRequestDto);
    }

    @PutMapping("/{id}")
    public PostResponseDto updatePost(@PathVariable Long id,
            @RequestBody PostRequestDto postRequestDto) {
            return postServiceImpl.updatePost(id, postRequestDto);
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deletePost(@PathVariable Long id,
            @RequestBody Map<String, String> password) {
            postServiceImpl.deletePost(id, password.get("password"));
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
}
