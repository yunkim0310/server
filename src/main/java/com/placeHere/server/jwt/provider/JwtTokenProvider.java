package com.placeHere.server.jwt.provider;

import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.CustomUser;
import com.placeHere.server.domain.User;
import com.placeHere.server.jwt.constants.JwtConstants;
import com.placeHere.server.jwt.prop.JwtProps;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private JwtProps jwtProps;

    @Autowired
    private UserDao userDao;

    /**
     * 토큰 생성
     * @param username
     * @param role
     * @return
     */
    public String createToken(String username, String role ) {
        
        // JWT 토큰 생성
        String jwt = Jwts.builder()
                // 서명에 사용할 키와 알고리즘 설정
                .signWith( getShaKey(), Jwts.SIG.HS512)
                // update (version : after 1.0)
                .header()
                // 헤더 설정
                .add("type", JwtConstants.TOKEN_TYPE)
                .and()
                // 토큰 만료 시간 설정 (10일)
                .expiration(new Date(System.currentTimeMillis() + 864000000))
                // payload : 클레임 설정: 유저네임
                .claim("username", "" + username)
                // payload : 클레임 설정: 권한
                .claim("role", role)
                .compact();

        log.info("jwt : " + jwt);

        return jwt;

    }

    /**
     * 🔐➡👩‍💼 토큰 해석
     *
     * Authorization : Bearer + {jwt}  (authHeader)
     * ➡ jwt 추출 : Bearer 걷어냄
     * ➡ UsernamePasswordAuthenticationToken
     *
     * 클라이언트 측에서 요청 정보가 들어올 때
     * 매개변수로 Bearer {jwt}가 들어온다. -> String authHeader 자리
     */
    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {

        if(authHeader == null || authHeader.length() == 0 ) {

            return null;
        }

        try {

            // jwt 추출 (Bearer + {jwt}) -> {jwt}
            String jwt = authHeader.replace("Bearer ", "");

            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("parsedToken : " + parsedToken);

            // 인증된 사용자 아이디
            String username = parsedToken.getPayload().get("username").toString();
            log.info("username : " + username);

            // 인증된 사용자 권한
            String role = parsedToken.getPayload().get("role").toString();
            log.info("role : " + role);


            // 토큰에 userId 있는지 확인
            // id 가 없으면 인증을 진행할 수 없기 때문이다.
            if( username == null || username.length() == 0 ) {
                return null;
            }

            // 유저정보 세팅
            User user = new User();
            user.setUsername(username);

            // OK: 권한도 바로 Users 객체에 담아보기
            user.setRole(role);

//            // OK
//            // CustomeUser 에 권한 담기
//            List<SimpleGrantedAuthority> authorities = ((List<?>) roles )
//                    .stream()
//                    .map(auth -> new SimpleGrantedAuthority( (String) auth ))
//                    .collect( Collectors.toList() );

            // 토큰 유효하면
            // 실제 DB에 있는 정보도 담아준다.
            // name, email 도 담아주기
            try {
                User userInfo = userDao.getUser(username);
                if( userInfo != null ) {
                    user.setUsername(userInfo.getUsername());
                    user.setEmail(userInfo.getEmail());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에러 발생...");
            }

            // 시큐리티에서 사용할 수 있는 객체로 감싸준다.
            UserDetails userDetails = new CustomUser(user);

            // OK
            // new UsernamePasswordAuthenticationToken( 사용자정보객체, 비밀번호 (우리는 없다.), 사용자의 권한(목록)  );
//            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            return new UsernamePasswordAuthenticationToken(userDetails, null);

        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", authHeader, exception.getMessage());
        }

        return null;
    }

    /**
     * 🔐❓ 토큰 유효성 검사
     * 기준 : 만료기간
     * @param jwt
     * @return
     *  ⭕ true     : 유효
     *  ❌ false    : 만료
     */
    public boolean validateToken(String jwt) {

        try {

            // 🔐➡👩‍💼 JWT 파싱
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);

            log.info("::::: 토큰 만료기간 :::::");
            log.info("-> " + parsedToken.getPayload().getExpiration());
            /*
                PAYLOAD
                {
                    "exp": 1703140095,        ⬅ 만료기한 추출
                    "uid": "joeun",
                    "rol": [
                        "ROLE_USER"
                    ]
                }
            */

            Date exp = parsedToken.getPayload().getExpiration();

            // 오늘날짜랑 비교
            // 만료시간과 현재 시간 비교
            // 2023.12.01 vs 2023.12.14
            // 만료면 true
            // 아니면 false
            return !exp.before(new Date());

        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");                 // 토큰 만료
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");                // 토큰 손상 -> 토큰이 변조된 것
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");                 // 토큰 없음
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // secretKey ➡ signingKey
    private byte[] getSigningKey() {

        // 가져온 secretKey를 byte로 변환한다.
        return jwtProps.getSecretKey().getBytes();
    }

    // secretKey ➡ (HMAC-SHA algorithms) ➡ signingKey
    // 가져온 secretKey(String) ➡ byte ➡ secretKey 객체로 변환
    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSigningKey());
    }
}
