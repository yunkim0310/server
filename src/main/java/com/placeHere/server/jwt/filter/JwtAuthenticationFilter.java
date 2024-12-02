package com.placeHere.server.jwt.filter;

import com.placeHere.server.domain.CustomUser;
import com.placeHere.server.jwt.constants.JwtConstants;
import com.placeHere.server.jwt.provider.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.swing.text.html.BlockView;
import java.io.IOException;

/**          (/login)
 * client -> filter -> server
 * ✅ username, password 인증시도 (attemptAuthentication)
 *
 * ❌ 인증실패 : response > status : 401 (UNAUTHORIZED)
 *
 * 👌 인증성공 (successfulAuthentication)
 *          -> JWT 생성
 *          -> response > headers > authorization : (jwt)
 */

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter( AuthenticationManager authenticationManager,  JwtTokenProvider jwtTokenProvider ) {
        System.out.println( "yunkim 11 " + authenticationManager);  // null
        System.out.println( "yunkim 22 " + jwtTokenProvider);
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        // 🔗 필터 URL 경로 설정 : /login
        setFilterProcessesUrl(JwtConstants.AUTH_LOGIN_URL);
        System.out.println("URL chk :: " + JwtConstants.AUTH_LOGIN_URL);
    }

    /**
     * 🔐 인증 시도 메소드
     * : /login 경로로 (username, password) 를 요청하면 이 필터에서 걸려 인증을 시도합니다.
     * ✅ Authentication 인증 시도한 사용자 인증 객체를 반환하여, 시큐리티가 인증 성공 여부를 판단하게 합니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        log.info("username : " + username);
        log.info("password : " + password);

        // 사용자 인증정보 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken( username, password);

        // role
        System.out.println("test 11 :: " + authentication.getPrincipal());
        // username
        System.out.println("test 22 :: " + authentication.getName());
        // password
        System.out.println("test 33 :: " + authentication.getCredentials());

        System.out.println("test 44 :: " + authentication.toString());
//        UsernamePasswordAuthenticationToken [Principal=user, Credentials=[PROTECTED], Authenticated=false, Details=null, Granted Authorities=[]]

        try {

            // 사용자 인증 (로그인)
            log.info("???????????? 11 ");

            // 여기서
            // CustomUserDetailService implements UserDetailsService 의
            // loadUserByUsername를 호출한다.

            authentication = authenticationManager.authenticate(authentication);

            log.info("???????????? 22 ");
            /*
                🔐 authenticate() 인증 처리 프로세스
                1️⃣ 주어진 Authentication 객체에서 사용자의 아이디를 추출한다.
                2️⃣ UserDetailsService를 사용하여 해당 아이디에 대한 UserDetails 객체를 가져온다.
                3️⃣ 가져온 UserDetails 객체에서 저장된 비밀번호를 확인하기 위해 PasswordEncoder를 사용한다.
                4️⃣ 사용자가 제공한 비밀번호와 저장된 비밀번호가 일치하는지 확인한다.
                5️⃣ 인증이 성공하면, 새로운 Authentication 객체를 생성하여 반환한다.
                ✅ 인증 여부를, isAuthenticated() ➡ true 로 확인할 수 있다.
             */

            log.info("authenticationManager : " + authenticationManager);
            log.info("authentication : " + authentication);
            log.info("인증 여부(isAuthenticated) : " + authentication.isAuthenticated());

        } catch (AuthenticationException e) {

            log.info(e.getMessage());

            // 인증 실패 (username, password 불일치)
            if( !authentication.isAuthenticated() ) {
                log.info("인증 실패 : 아이디와 비밀번호가 일치하지 않습니다.");
                log.error("인증실패", e);
                response.setStatus(401);
            }

            // 인증 실패 시 null 반환
            return null;

        }

        return authentication;
    }


    /**
     * ⭕ 인증 성공 메소드
     * : attemptAuthentication() 호출 후, 반환된 Authentication - 사용자 인증 객체가 인증된 것이 확인되면, 호출됩니다.
     *
     * - jwt를 생성
     * - jwt 를 응답헤더에 설정
     *
     * ➡ 🔐 JWT
     * : 로그인 인증에 성공했으므로, JWT 토큰을 생성하여
     *   응답(response) 헤더에 jwt 토큰을 담아 응답합니다.
     *   💍 { Authorization : Bearer + {jwt} }
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("인증 성공...");

        CustomUser user = (CustomUser) authentication.getPrincipal();
        String username = user.getUser().getUsername();

        String role = user.getUser().getRole();

        System.out.println("successfulAuthentication ::");
        System.out.println("username :: " + username);
        System.out.println("role :: " + role);


        // 💍 JWT 토큰 생성 요청
        String jwt = jwtTokenProvider.createToken(username, role);

        // 💍 { Authorization : Bearer + {jwt} }
        response.addHeader(JwtConstants.TOKEN_HEADER, JwtConstants.TOKEN_PREFIX + jwt);
        response.setStatus(200);

        if ( user.getUser().getRole().equals("ROLE_USER") || user.getUser().getRole().equals("ROLE_STORE")) {
            response.sendRedirect("/");
        }

    }
}
