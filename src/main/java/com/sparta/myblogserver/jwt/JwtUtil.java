package com.sparta.myblogserver.jwt;

import com.sparta.myblogserver.entity.user.UserRoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;

/**
 * JWT 반환 방법 2가지
 * 1. JWT 를 response 헤더에 바로 담는 방법
 * 2. 쿠키를 생성해서 쿠키안에 JWT를 넣고 response 에 쿠키를 담는 방법
 *
 */

@Slf4j(topic = "JWT 관련 로그")
@Component
public class JwtUtil {

    // 쿠키의 name 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long Expiration_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // SecretKey
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * JWT 토큰을 생성하는 메소드
     *
     * @param username 사용자 username
     * @param role     사용자 role
     * @return "Bearer " + 사용자 식별 정보를 담은 JWT 토큰
     */
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한 key-value 로 claim에 값을 넣을 수 있다.
                        .setExpiration(
                                new Date(date.getTime() + Expiration_TIME)) // 만료 시간 (현재 시간 + 만료 시간)
                        .setIssuedAt(date) // 발급일
                        .signWith(key,
                                signatureAlgorithm) // 암호화 알고리즘 여기서 key 값은 기존 jwt.secret.key를 디코딩한 값.
                        .compact();
    }


    /**
     * 받아온 토큰 값의 공백을 "%20" 으로 변환 한 이후 쿠키에 값을 넣고 response 객체에 응답함.
     *
     * @param token 실제 JWT 토큰 값
     * @param res   Client 에게 보낼 응답
     */
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8")
                    .replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 받아온 토큰 값의 공백을 "%20" 으로 변환 한 이후 ResponseHeader 에 JWT 값을 넣고 응답 반환.
     *
     * @param token 실제 JWT 토큰 값
     * @param res   Client 에게 보낼 응답
     */
    public void addJwtToHeader(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8")
                    .replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            res.setHeader(AUTHORIZATION_HEADER, token);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * JWT 토큰 앞의 "Bearer " 부분을 잘라내는 메소드
     *
     * @param tokenValue 쿠키에 들어있는 토큰의 전체 값 "Bearer " 로 시작
     * @return 토큰 시작 부분의 "Bearer "를 제거한 순수한 JWT 값 반환
     */
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("토큰을 찾을 수 없습니다.");
        throw new NullPointerException("토큰을 찾을 수 없습니다.");
    }

    /**
     * JWT 토큰의 유효성을 검증하는 메소드
     *
     * @param token substring 한 실제 JWT 토큰 값
     * @return 유효하다면 true, 아니면 false 반환
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    /**
     * 검증받은 token을 통해 사용자 정보를 가져오는 메소드
     *
     * @param token
     * @returm token을 통해 조회한 사용자의 정보
     */
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * request 내부 쿠키에서 JWT를 가져오는 메소드
     *
     * @param req 요청으로 들어온 HttpRequest
     * @return decode된 JWT 값
     */
    public String getTokenFromRequestCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(),
                                "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * request 헤더에서 JWT를 가져오는 메소드
     *
     * @param req 요청으로 들어온 HttpRequest
     * @return decode된 JWT 값
     */
    public String getTokenFromRequestHeader(HttpServletRequest req) {
        String token = req.getHeader(AUTHORIZATION_HEADER);
        if (token != null) {
            try {
                return URLDecoder.decode(token, "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }

}


