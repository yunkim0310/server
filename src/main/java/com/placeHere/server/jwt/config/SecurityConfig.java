package com.placeHere.server.jwt.config;

import com.placeHere.server.jwt.filter.JwtAuthenticationFilter;
import com.placeHere.server.jwt.filter.JwtRequestFilter;
import com.placeHere.server.jwt.provider.JwtTokenProvider;
import com.placeHere.server.service.jwt.CustomUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        System.out.println("SecurityConfig - authenticationManager");
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
        System.out.println( "SecurityConfig - authenticationManager :: " + authenticationManager);
        return authenticationManager;
    }


    @Bean
    @DependsOn("authenticationManager") // 빈 순서 지켜야함
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        log.info("securityFilterChain...");

        http
                .cors(cors -> cors
                        .configurationSource(new CorsConfigurationSource() {

                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                                CorsConfiguration configuration = new CorsConfiguration();

                                // 허용할 서버포트
                                configuration.setAllowedOrigins( Collections.singletonList( "http://localhost:3000"));
                                // get, post, option 등
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                configuration.setMaxAge(3600L);

                                // 헤더에 Authorization 넘겨줄 수 있게 허용
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }));

        // // 폼 기반 로그인 비활성화
        http.formLogin( login -> login.disable() );

        // // HTTP 기본 인증 비활성화
        http.httpBasic( basic -> basic.disable() );

        // CSRF(Cross-Site Request Forgery) 공격 방어 기능 비활성화
        http.csrf( csrf -> csrf.disable() );

        /**
         * ㅇ필터설정 (순서)
         * (1) *Jwt Request* Filter : jwt 토큰을 포함해서 요청하는 필터
         * - jwt 토큰을 해석하는 작업
         *
         * (2) Jwt Filter : 인증하는 필터
         * - login진행
         * - username, password를 가져와서 인증을 보내고 인증이 완료되면
         *   -> jwt token 생성
         *
         * ㅇ인가설정
         * - 정적자원 ALL
         * - /, /login ALL
         * - /user/** user, admin
         * - /admin/** admin
         */
        // 필터 설정 ✅
//                        (2)
        System.out.println( "SIBAL :: " + authenticationManager);
        http.addFilterAt(new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider) , UsernamePasswordAuthenticationFilter.class)
                                    // (1)
                .addFilterBefore(new JwtRequestFilter(jwtTokenProvider) , UsernamePasswordAuthenticationFilter.class);

        /**
         * 인가설정
         */
        http.authorizeHttpRequests( authorizeRequests ->
                authorizeRequests
                        // 정적자원 경로 다 허용 (static)
                        // 프론트랑 백이랑 분리되어 있다면 굳이 필요 x
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        // UserController에서 이미 권한을 관리하고 있기 때문에 여기서 우선 permitAll
//                        .requestMatchers("/users/**").hasAnyRole("USER" , "ADMIN")
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/store/**").hasRole("STORE")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/store/**").hasRole("STORE")
                        .anyRequest().authenticated() )
        ;
        /**
         * 인증방식 설정
         * 사용자의 정보를 비즈니스 로직에서 어떻게 작성할건지 정하는 시큐리티 구조이다.
         * userDetailsService를 구현한 클래스를 넣어주면 된다.
         *
         * 우리는 커스텀 방식으로 db조회해서 가져온다.
         */
        http.userDetailsService(customUserDetailService);

        return http.build();
    } // end of securityFilterChain

    /**
     * 암호화 방식
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
