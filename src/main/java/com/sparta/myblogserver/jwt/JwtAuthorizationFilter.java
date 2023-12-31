package com.sparta.myblogserver.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblogserver.dto.response.ErrorResponse;
import com.sparta.myblogserver.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
            FilterChain filterChain) throws ServletException, IOException {

        try {

            if (!"GET".equals(req.getMethod()) && !req.getRequestURI().equals("/api/users/login") && !req.getRequestURI().equals("/api/users/signup")) { // GET 요청이거나 로그인 시도가 아니라면 토큰 유무 및 유효성 검증

                // 토큰 체크
                String tokenValue = jwtUtil.getTokenFromRequestHeader(req);

                // JWT 토큰 "Bearer " 부분 substring
                tokenValue = jwtUtil.substringToken(tokenValue);

                // 토큰 유효성 검사
                jwtUtil.validateToken(tokenValue);

                // JWT에서 유저 정보 받아오기
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

                // SecurityContext 내부에 들어갈 Authentication 설정
                setAuthentication(info.getSubject());
            }

            filterChain.doFilter(req, res);
        } catch (JwtException e) {
            errorResponse(res, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            errorResponse(res, HttpStatus.INTERNAL_SERVER_ERROR, "요청중 에러가 발생하였습니다.");
        }

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

    private static void errorResponse(HttpServletResponse res, HttpStatus httpStatus,
            String message) throws IOException {
        res.setStatus(httpStatus.value());
        res.setContentType("application/json;charset=UTF-8");
        ErrorResponse responseMessage = new ErrorResponse(HttpStatus.BAD_REQUEST,
                message);

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = res.getWriter();
        objectMapper.writeValue(out, responseMessage);
    }
}