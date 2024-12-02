package com.placeHere.server.jwt.filter;

import com.placeHere.server.jwt.constants.JwtConstants;
import com.placeHere.server.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // 생성자
    public JwtRequestFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 헤더에서 jwt 토큰을 가져옴
        String header = request.getHeader(JwtConstants.TOKEN_HEADER);

        String uri = request.getRequestURI();
        String method = request.getMethod();
        log.info("URI CHECK :: " + uri);
        log.info("METHOD CHECK :: " + method);

        // Bearer 없이 들어옴
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MzM2Mjk3OTYsInVubyI6IjEiLCJ1aWQiOiJ1c2VyIiwicm9sIjpbIlJPTEVfVVNFUiJdfQ.OBghAHxzgmZLc_DsryjBlvLxWU8_4rtB9_o9i12YG0oW9Et7vdP4nldTJVRjDpjlmCYfUj02lctWz950liMdCw
        log.info("authorization : " + header);

        // Bearer + {jwt} 체크
        // 토큰이 이상하면 다음 필터에게 넘김
//        if ( header == null || header.length() == 0 || header.startsWith(JwtConstants.TOKEN_PREFIX ) ) {
        if ( header == null || header.length() == 0 || header.startsWith(JwtConstants.TOKEN_PREFIX ) || (uri.equals("/login") && method.equals("GET") )) {
            log.info("111");
            filterChain.doFilter(request, response);
            log.info("222");
            return;
        }

        // 토큰이 정상이라면
        // Bearer + {jwt}에서 Bearer제거
        log.info("333");
        log.info("header :: " + header);
        String jwt = header.replace(JwtConstants.TOKEN_PREFIX, "");
        log.info("444");

        // 토큰해석
        // jwtTokenProvider에게 토큰이 유효한지 체크한다.
        Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
        log.info("555");

        // 토큰 유효성 검사
        if ( jwtTokenProvider.validateToken(jwt) ) {
            log.info("666");
            log.info("유효한 JWT 토큰입니다.");
            log.info("777");

            // 로그인
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("888");
        }

        // 다음 필터
        filterChain.doFilter(request, response);

    }
    
}
