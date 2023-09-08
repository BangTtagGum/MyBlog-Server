package com.sparta.myblogserver.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.myblogserver.dto.user.LoginRequestDto;
import com.sparta.myblogserver.entity.user.UserRoleEnum;
import com.sparta.myblogserver.dto.response.BaseResponse;
import com.sparta.myblogserver.security.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * UsernamePasswordAuthenticationFilter 는 기본적으로 Session방식이라
 * JWT를 사용하기 위해 직접 Customizing
 */
@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToHeader(token, response);

        // JSON 응답을 생성
        setResponse(response, HttpStatus.OK,"로그인 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed)
            throws IOException {

        log.info("로그인 실패");
        // JSON 응답을 생성
        setResponse(response,HttpStatus.UNAUTHORIZED,"로그인 정보가 일치하지 않습니다.");
    }

    /**
     * 로그인 후 성공, 실패 여부 응답 생성하는 메소드
     *
     * @param response response 객체
     * @param status 설정하고 싶은 http 상태코드
     * @param message 보내고 싶은 메세지
     * @throws IOException
     */
    private static void setResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        BaseResponse baseResponse = new BaseResponse(status, message);

        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        objectMapper.writeValue(out, baseResponse);
    }
}
