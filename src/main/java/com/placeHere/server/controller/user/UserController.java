//package com.placeHere.server.controller.user;
//
//
//import com.placeHere.server.domain.User;
//import com.placeHere.server.service.user.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//@Controller
//@ResponseBody
////@RequestMapping("/user")
//public class UserController {
//
//    @Autowired
//    @Qualifier("userServiceImpl")
//    private UserService userService;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder; // BCryptPasswordEncoder 주입
//
//
//    @GetMapping("/")
//    public String mainP() {
//
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
//    }
//
//    @PostMapping("/login")
//    public String login (String username, String password) throws Exception {
//
//        System.out.println("이거타나?");
//
//        User user = userService.login(username);
//
//        if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
//            throw new AuthenticationException("Invalid credentials") {
//            };
//        }
//
//        userService.updateLoginDt(username);
//
//        return "login ok";
//    }
//
//    @PostMapping("/join")
//    public String joinP (User user) throws Exception {
//
//        userService.join(user);
//
//        return "joink ok";
//    }
//
//    @GetMapping("/admin")
//    public String adminP() {
//
//        return "admin Conroller";
//    }
//}
