package com.sparta.myblogserver.domain.post.contorller;


import com.sparta.myblogserver.domain.post.dto.PostReq;
import com.sparta.myblogserver.domain.post.dto.PostRes;
import com.sparta.myblogserver.domain.post.service.PostServiceImpl;
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
    public List<PostRes> findAllPosts() {
        return postServiceImpl.findAllPosts();
    }

    @GetMapping("/{id}")
    public PostRes findPostById(@PathVariable Long id) {
        return postServiceImpl.findPostById(id);
    }

    @PostMapping
    public PostRes createPost(@RequestBody PostReq postReq) {
            return postServiceImpl.createPost(postReq);
    }

    @PutMapping("/{id}")
    public PostRes updatePost(@PathVariable Long id,
            @RequestBody PostReq postReq) {
            return postServiceImpl.updatePost(id, postReq);
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
