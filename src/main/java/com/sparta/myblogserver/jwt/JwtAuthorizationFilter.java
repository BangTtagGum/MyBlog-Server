package com.sparta.myblogserver.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblogserver.controller.message.Message;
import com.sparta.myblogserver.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getTokenFromRequestHeader(req);

        // 이미 인증되어 JWT가 헤더에 들어있을 경우
        if (StringUtils.hasText(tokenValue)) {

            // JWT 토큰 "Bearer " 부분 substring
            tokenValue = jwtUtil.substringToken(tokenValue);

            // 토큰 유효성 검사
            if (!jwtUtil.validateToken(tokenValue)) {
                res.setStatus(400);
                res.setContentType("application/json;charset=UTF-8");
                Message responseMessage = new Message(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.");

                ObjectMapper objectMapper = new ObjectMapper();
                PrintWriter out = res.getWriter();
                objectMapper.writeValue(out, responseMessage);
                return;
            }

            // JWT에서 유저 정보 받아오기
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                // SecurityContext 내부에 들어갈 Authentication 설정
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }
}