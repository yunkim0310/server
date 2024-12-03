package com.placeHere.server.controller.user;


import com.placeHere.server.domain.CustomUser;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Controller
//@ResponseBody
//@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    // BCryptPasswordEncoder 주입
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String mainP() {

//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // ROLE 확인
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
//        GrantedAuthority auth = iter.next();
//        String role = auth.getAuthority();
//
//        return "Main Controller : "+username + role;
        return "index";
    }

    @GetMapping("/user/loginView")
    public String login () throws Exception {

        log.info("login page plz.....");
        return "/user/loginView";
    }

    /**
     * 사용자 정보 조회
     * @param customUser
     * @return
     */
    // USER 권한 설정
//    @Secured("ROLE_USER")
    @GetMapping("/user/info")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal CustomUser customUser) {

        log.info("getUser Controllr :: ");

        log.info("::::: customUser :::::");
        log.info("customUser : "+ customUser);

        User user = customUser.getUser();
        log.info("user : " + user);

        // 인증된 사용자 정보
        if( user != null ) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

        // 인증 되지 않음
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }


    @PostMapping("/join")
    public String joinP (User user) throws Exception {

        userService.join(user);

        return "joink ok";
    }

    @GetMapping("/admin")
    public String adminP() {

        return "admin Conroller";
    }

}
