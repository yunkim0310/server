package com.placeHere.server.controller.user;

import com.placeHere.server.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping("/login")
    public String login () {

        log.info(" 로그인 페이지 ~~  ");

        return "user/loginView";

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

        // 새로운 User 객체를 모델에 추가
//        model.addAttribute("user", new User());
        log.info("얼라리요");

        return "user/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("user") User user) {

        log.info("join Controller 호출");

        log.info("User 객체 :: " + user);

        return "/";
    }

}
