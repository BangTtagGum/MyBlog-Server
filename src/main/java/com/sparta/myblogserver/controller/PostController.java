package com.sparta.myblogserver.controller;


import com.sparta.myblogserver.controller.message.Message;
import com.sparta.myblogserver.controller.message.SuccessMessage;
import com.sparta.myblogserver.dto.post.PostRequestDto;
import com.sparta.myblogserver.dto.post.PostResponseDto;
import com.sparta.myblogserver.error.ParameterValidationException;
import com.sparta.myblogserver.security.UserDetailsImpl;
import com.sparta.myblogserver.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Message> findAllPosts() {

        List<PostResponseDto> postResponseDtos = postService.findAllPosts();
        return ResponseEntity.ok().body(new SuccessMessage("전체 게시물 조회 성공", postResponseDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findPostById(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.findPostById(id);
        return ResponseEntity.ok().body(new SuccessMessage("게시물 조회 성공 Post ID: " + id,postResponseDto));
    }

    /**
     * 게시글 작성 메소드 - 헤더에 JWT 토큰 필요
     *
     * @param postRequestDto 작성한 게시글 내용
     * @param userDetails    인증된 사용자 정보
     * @return 저장된 게시글 반환
     */
    @PostMapping
    public ResponseEntity<Message> createPost(@RequestBody @Valid PostRequestDto postRequestDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
        // 게시글에 작성자 이름 추가
        postRequestDto.addAuthor(userDetails.getUsername());
        PostResponseDto postResponseDto = postService.createPost(postRequestDto);
        return ResponseEntity.ok().body(new SuccessMessage("게시물 생성 성공", postResponseDto));
    }

    /**
     * 게시글 수정 메소드 - 헤더에 JWT 토큰 필요
     *
     * @param postRequestDto 작성한 게시글 내용
     * @return 수정된 게시글 반환
     */
    @PutMapping("/{id}")
    public ResponseEntity<Message> updatePost(@PathVariable Long id,
            @RequestBody @Valid PostRequestDto postRequestDto, BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }

        PostResponseDto postResponseDto = postService.updatePost(id, postRequestDto,
                userDetails.getUsername());
        return ResponseEntity.ok().body(new SuccessMessage("게시물 수정 성공",postResponseDto));
    }

    /**
     * 게시글 삭제 메소드 - 헤더에 JWT 토큰 필요
     *
     * @param id 게시물 id
     * @return 성공, 실패 여부 Message 반환
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<Message> deletePost(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long deletedPostId = postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.ok()
                .body(new SuccessMessage("게시물 삭제 성공 Post ID: " + deletedPostId));
    }

}