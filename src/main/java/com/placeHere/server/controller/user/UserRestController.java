package com.placeHere.server.controller.user;

import com.placeHere.server.domain.User;
import com.placeHere.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api-user")
public class UserRestController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    // getUser
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam(value="username") String username) throws Exception {

        log.info("username");

        log.info("/api-user/getUser - GET Controller ");

        User user = userService.getUser(username);

        if ( user != null ) {
        log.info(" >>> 가져온 user :: " + user );
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chkDuplication")
    public ResponseEntity<?> chkDuplication(@RequestParam(value="username") String username) throws Exception {

        log.info("username :: " + username);

        boolean result = userService.chkDuplication(username);

        // 이미 회원이 존재하면 true, 반대는 false

        // boolean 값 리턴
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/chkEmail")
    public ResponseEntity<?> chkEmail(@RequestParam(value="email") String email) throws Exception {

        log.info("email :: " + email);

        boolean result = userService.chkEmail(email);

        log.info("이메일 존재하는 지 값 확인 :: " + result);

        // 이미 회원이 존재하면 true, 반대는 false

        // boolean 값 리턴
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/join")
    public ResponseEntity<?> join( @RequestBody User user) throws Exception{

        log.info("join Controller - post 호출");
        log.info("회원가입 User 객체 :: " + user);

        // todo, 성별, 생년월일 누락

        int result = userService.join(user);

        log.info("회워가입 result :: " + result);

        if( result > 0 ) {
            log.info("회원가입 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
        else {
            log.info("회원가입 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }









}
