package com.placeHere.server.controller.user;

import com.google.zxing.qrcode.decoder.Mode;
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

        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        log.info("logout Controller - get 호출");
        session.invalidate();

        return "index";
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

        return "user/join";
    }


    @PostMapping("/join")
    public String join( @ModelAttribute("user") User user ) throws Exception{

        log.info("join Controller - post 호출");
        log.info("User 객체 :: " + user);

        userService.join(user);

        return "user/loginView";
    }

    @GetMapping("/getUser")
    public String getUser(@ModelAttribute("user") User user) {

        log.info("getUser Controller - get 호출");
        log.info("user :: " + user);

        return "test/user/getUserTest";
    }

    @GetMapping("/resetPwdValidation")
    public String resetPwdValidation() {

        log.info("resetPwdValidation - get 요청");

        return "user/resetPwdValidation";
    }

    @PostMapping("/resetPwdValidation")
    public String resetPwdValidation( @ModelAttribute("user") User user, HttpSession session, Model model) throws Exception{

        log.info("resetPassword - post 요청");
        log.info(">>> INPUT USER CHECK :: " + user);

        user = userService.getUser(user.getUsername());

        boolean result = userService.resetPwdValidation(user);

        if ( result ) {
            log.info(" resetPwdValidation OK ");
            log.info(" user chk :: " + user);
            session.setAttribute("user", user);
//            model.addAttribute("result", Boolean.valueOf(result) );
            return "user/resetPwd";

        } else {
            log.info(" resetPwdValidation NOK ");
            model.addAttribute("error", "정보가 일치하지 않습니다.");
            return "user/resetPwd";
        }
    }

    @GetMapping("/resetPwd")
    public String resetPwd(HttpSession session) {

        User user = (User) session.getAttribute("user");

        session.getAttribute("user :: " + user);
        log.info("resetPwd - get 요청");


        return "user/resetPwd";
    }

    @PostMapping("/resetPwd")
    public String resetPwd(@RequestParam("password") String password,
                           HttpSession session, Model model) throws Exception {

        log.info("resetPwd - post 요청");

        // 세션에서 사용자 정보 가져오기
        User user = (User) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("error", "사용자 정보를 찾을 수 없습니다.");
            return "user/resetPwdValidation";  // 세션에 사용자 정보가 없으면 다시 첫 번째 단계로 돌아감
        } else {

            log.info("user 정보 확인 ");
            log.info( "username :: " + user.getUsername() );
            log.info( "password :: " + user.getPassword() );

            userService.updatePwd(user);
        }


        // 비밀번호 변경 후, 세션에서 사용자 정보 삭제
        session.removeAttribute("user");

        return "user/loginView";
    }

    @GetMapping("/setting")
    public String setting(HttpSession session, Model model) {

        log.info("setting - get 요청");
        User user = (User) session.getAttribute("user");

        log.info("user :: " + user);

        session.getAttribute("setting user chk :: " + user);
        model.addAttribute("user", user);

        return "user/setting";
    }



}
