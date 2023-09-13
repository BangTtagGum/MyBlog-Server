package com.sparta.myblogserver.controller;


import com.sparta.myblogserver.dto.post.PostRequestDto;
import com.sparta.myblogserver.dto.post.PostResponseDto;
import com.sparta.myblogserver.dto.response.BaseResponse;
import com.sparta.myblogserver.dto.response.SuccessResponse;
import com.sparta.myblogserver.error.ParameterValidationException;
import com.sparta.myblogserver.security.UserDetailsImpl;
import com.sparta.myblogserver.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    /**
     * 게시물 전체 조회 메소드 - JWT 토큰 필요 X
     *
     * @return 조회된 게시글 리스트 반환
     */
    @GetMapping
    public ResponseEntity<BaseResponse> findAllPosts() {

        List<PostResponseDto> postResponseDtos = postService.findAllPosts();
        return ResponseEntity.ok().body(new SuccessResponse("전체 게시물 조회 성공", postResponseDtos));
    }

    /**
     * 게시글 단일 조회 메소드 - JWT 토큰 필요 X
     *
     * @param id 찾고싶은 게시글의 id 값
     * @return 조회된 게시글 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findPostById(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.findPostById(id);
        return ResponseEntity.ok()
                .body(new SuccessResponse("게시물 조회 성공 Post ID: " + id, postResponseDto));
    }

    /**
     * 게시글 작성 메소드 - 헤더에 JWT 토큰 필요
     *
     * @param postRequestDto 작성한 게시글 내용
     * @param userDetails    인증된 사용자 정보
     * @return 저장된 게시글 반환
     */
    @PostMapping
    public ResponseEntity<BaseResponse> createPost(
            @RequestBody @Valid PostRequestDto postRequestDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);
        // 게시글에 작성자 이름 추가
        PostResponseDto postResponseDto = postService.createPost(postRequestDto,
                userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("게시물 생성 성공", postResponseDto));
    }

    /**
     * 게시글 수정 메소드 - 헤더에 JWT 토큰 필요
     *
     * @param postRequestDto 작성한 게시글 내용
     * @return 수정된 게시글 반환
     */
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updatePost(@PathVariable Long id,
            @RequestBody @Valid PostRequestDto postRequestDto, BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        PostResponseDto postResponseDto = postService.updatePost(id, postRequestDto,
                userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse("게시물 수정 성공", postResponseDto));
    }

    /**
     * 게시글 삭제 메소드 - 헤더에 JWT 토큰 필요
     *
     * @param id 게시물 id
     * @return 성공 Message 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deletePost(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long deletedPostId = postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok()
                .body(new SuccessResponse("게시물 삭제 성공 Post ID: " + deletedPostId));
    }

    /**
     * 게시물 좋아요 토글 메소드 - 헤더에 JWT 토큰 필요 이미 좋아요를 눌렀던 게시글이라면
     *
     * @param id 좋아요를 누를 게시물 id
     * @return Message 반환
     */
    @PostMapping("/{id}/likes")
    public ResponseEntity<BaseResponse> likePost(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String responseMessage = postService.likePostToggle(id, userDetails.getUser());
        return ResponseEntity.ok().body(new SuccessResponse(
                responseMessage + " 게시물 id: " + id + " 유저 id: " + userDetails.getUser().getId()));
    }

    /**
     * 파라미터 Validation 메소드 Valid 결과로 나온 Exception을 메세지와 함께 ExHandler에게 전달
     *
     * @param bindingResult Parameter Validation중 발생한 Exception 모음
     */
    private static void checkParamValidation(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }

}