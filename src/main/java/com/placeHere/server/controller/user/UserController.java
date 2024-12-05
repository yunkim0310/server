package com.placeHere.server.controller.user;

import com.placeHere.server.domain.User;
import com.placeHere.server.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    ///Field
    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @GetMapping("/login")
    public String login () {

        log.info("login Controller - get 호출");

        return "user/loginView";
//        return "test/user/loginTest";

    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user , HttpSession session ) throws Exception {

        log.info("login Controller - post 호출");
        log.info(" 요청된 data " + user);

        log.info(user.getUsername());
        log.info(user.getPassword());

        User dbUser = userService.getUser(user.getUsername());
        log.info("login user :: " + user);

        // TODO 비밀번호 암호화 하기
        if( user.getPassword().equals(dbUser.getPassword())){
            session.setAttribute("user", dbUser);
        }

        return "/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        log.info("logout Controller - get 호출");
        session.invalidate();

        return "/index";
    }

    @GetMapping("/selectRole")
    public String join() {

        log.info("join Controller 호출");

        return "user/selectRole";
    }

    @GetMapping("/join")
    public String join(@RequestParam(name = "role") String role, Model model) {

        log.info("join Controller - get 호출");
        log.info("param :: " + role);

        model.addAttribute("role", role);

        return "user/join?role="+role;
    }


    @PostMapping("/join")
    public String join(@ModelAttribute("user") User user) {

        log.info("join Controller - post 호출");

        log.info("User 객체 :: " + user);


        return "/";
    }

    @GetMapping("/getUser")
    public String getUser() {


        
        log.info("getUser Controller - get 호출");
        return "test/user/getUserTest";
    }

}
