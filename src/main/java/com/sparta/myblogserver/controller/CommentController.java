package com.sparta.myblogserver.controller;

import com.sparta.myblogserver.dto.post.comment.CommentRequestDto;
import com.sparta.myblogserver.dto.response.BaseResponse;
import com.sparta.myblogserver.dto.response.SuccessResponse;
import com.sparta.myblogserver.entity.user.User;
import com.sparta.myblogserver.error.ParameterValidationException;
import com.sparta.myblogserver.security.UserDetailsImpl;
import com.sparta.myblogserver.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<BaseResponse> createComment(@PathVariable Long postId,
            @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 생성 성공",
                        commentService.createComment(postId, requestDto,userDetails.getUser())));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<BaseResponse> updateComment(@PathVariable Long id,
            @RequestBody @Valid CommentRequestDto requestDto, BindingResult bindingResult,
            @AuthenticationPrincipal
            UserDetailsImpl userDetails) {
        checkParamValidation(bindingResult);

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 수정 성공",
                        commentService.updateComment(id, requestDto, userDetails.getUser())));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<BaseResponse> deleteComment(@PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(id, userDetails.getUser());

        return ResponseEntity.ok()
                .body(new SuccessResponse("댓글 삭제 성공 + Comment Id: " + id));
    }

    private static void checkParamValidation(BindingResult bindingResult) {

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError e : fieldErrors) {
            throw new ParameterValidationException(e.getDefaultMessage());
        }
    }
}
