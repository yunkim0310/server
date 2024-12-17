package com.placeHere.server.controller.user;

import com.placeHere.server.domain.User;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.store.StoreService;
import com.placeHere.server.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api-user")
public class UserRestController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("communityServiceImpl")
    private CommunityService communityService;

    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

    @Autowired
    private ReservationService reservationService;


    // getUser
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam(value = "username") String username) throws Exception {

        log.info("username");

        log.info("/api-user/getUser - GET Controller ");

        User user = userService.getUser(username);

        if (user != null) {
            log.info(" >>> 가져온 user :: " + user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpSession session) throws Exception {

        log.info("/api-user/login - POST Controller ");


        User dbUser = userService.getUser(user.getUsername());

        String activeStatus;

        if ( dbUser != null && user.getPassword().equals(dbUser.getPassword()) ) {
            activeStatus = dbUser.getActiveStatus();
            log.info("activeStatus :: " + activeStatus);


            if (activeStatus.equals("ACTIVE")) {
                log.info(" ACTIVE USER ");
                session.setAttribute("user", dbUser);

                // 휴면계정이라면
            } else if (activeStatus.equals("INACTIVE")) {
                log.info(" INACTIVE USER ");
                return new ResponseEntity<>("INACTIVE", HttpStatus.OK);
                // 탈퇴계정이라면
            } else if (activeStatus.equals("DELETED")) {
                log.info(" DELETED USER ");
                return new ResponseEntity<>("DELETED", HttpStatus.OK);
            }
        } else { // 로그인 정보 없음
            return new ResponseEntity<>("FAIL", HttpStatus.OK);
        }
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }



    @GetMapping("/chkDuplication")
    public ResponseEntity<?> chkDuplication(@RequestParam(value = "username") String username) throws Exception {

        log.info("username :: " + username);

        boolean result = userService.chkDuplication(username);

        // 이미 회원이 존재하면 true, 반대는 false

        // boolean 값 리턴
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/chkEmail")
    public ResponseEntity<?> chkEmail(@RequestParam(value = "email") String email) throws Exception {

        log.info("email :: " + email);

        boolean result = userService.chkEmail(email);

        log.info("이메일 존재하는 지 값 확인 :: " + result);

        // 이미 회원이 존재하면 true, 반대는 false

        // boolean 값 리턴
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody User user) throws Exception {

        log.info("join Controller - post 호출");
        log.info("회원가입 User 객체 :: " + user);

        // todo, 성별, 생년월일 누락

        int result = userService.join(user);

        log.info("회워가입 result :: " + result);

        if (result > 0) {
            log.info("회원가입 성공! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("회원가입 실패! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(@RequestBody User user) throws Exception {

        log.info("updateProfile Controller - post 호출");
        log.info("updateProfile input >>  " + user);

        int result = userService.updateProfile(user);

        log.info("updateProfile result :: " + result);

        if (result > 0) {
            log.info("updateProfile ! - SUCCESS");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("updateProfile ! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resetPwd")
    public ResponseEntity<?> resetPwd(@RequestBody User user, HttpSession session) throws Exception {

        log.info("resetPwd - post 요청");
        log.info(">> resetPwd input user 확인 :: " + user);

        int result = 0;
        result = userService.updatePwd(user);

        if (result > 0) {
            log.info("resetPwd ! - SUCCESS");
            session.invalidate();
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        } else {
            log.info("resetPwd ! - FAIL");
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/goodByeRsrvCnt")
    public ResponseEntity<?> goodByeRsrvCnt(@RequestBody User user, HttpSession session) throws Exception {

        log.info("goodByeRsrvCnt - post 요청");

        String username = user.getUsername();
        String role = user.getRole();

        int rsrvCnt = 0;
        int phoneRsrvCnt = 0;

        log.info("username :: " + username);
        log.info("role :: " + role);

        Map<String, Object> result = new HashMap<>();

        // 일반회원
        if ( role.equals("ROLE_USER") ) {

            // 일반회원 예약 cnt
            Integer rsrvCount = reservationService.getCountRsrvUser(username);
            rsrvCnt = rsrvCount != null ? rsrvCount : 0;


            result.put("rsrvCnt", rsrvCnt);
            result.put("status", "SUCCESS");
            result.put("role", role);

            return ResponseEntity.ok(result);

        // 점주회원
        } else {

            // username을 통해 storeId get
            Integer storeId = storeService.getStoreId(username);

            // storeId가 유효할 경우 예약 cnt와 전화 예약 cnt 조회
            if (storeId != null && storeId > 0) {

                Integer rsrvCount = reservationService.getCountRsrvStore(storeId);
                Integer phoneRsrvCount = reservationService.getCountRsrvNumber(storeId);

                rsrvCnt = rsrvCount != null ? rsrvCount : 0;
                phoneRsrvCnt = phoneRsrvCount != null ? phoneRsrvCount : 0;
            }

            result.put("rsrvCnt", rsrvCnt);
            result.put("phoneRsrvCnt", phoneRsrvCnt);
            result.put("status", "SUCCESS");
            result.put("role", role);

            return ResponseEntity.ok(result);

        }

    }

    @PostMapping("/goodBye")
    public ResponseEntity<?> goodBye(@RequestBody User user, HttpSession session) throws Exception {

        log.info("goodBye - post 요청");
        log.info(">> goodBye input user 확인 :: " + user);

        String username = user.getUsername();
        String role = user.getRole();

        user.setActiveStatus("DELETED");
        int commentResult = 0;
        int reviewResult = 0;
        int storeId = 0;

        boolean refundResult = false;


        // 일반회원
        if (user.getRole() == "ROLE_USER" || user.getRole().equals("ROLE_USER")) {
            log.info("일반회원 탈퇴");

            // 댓글삭제(상태값 변경) 1 -> 0
            commentResult = communityService.deleteAllCommentsByUser(user.getUsername() );
            // 리뷰삭제(상태값 변경) 1 -> 0
            reviewResult = communityService.deleteAllReviewsByUser(user.getUsername() );

            log.info("commentResult :: " + commentResult);
            log.info("reviewResult :: " + reviewResult);

            // 예약 환불 요청
            refundResult = reservationService.getRemoveUserRefundPayment(username);

            if ( refundResult ) {

                log.info("goodBye User SUCCESS ! ");
                // 회원 상태값 변경
                userService.updateUserStatus(user);

                // 세션 삭제
                session.invalidate();
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            } else {

                log.info("goodBye User FAIL ! ");
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }

            // 점주 회원
        } else {
            log.info("점주회원 탈퇴");
            storeId = storeService.getStoreId(user.getUsername() );
            log.info("storeId :: " + storeId);

            // 가게 상태값 변경 0 -> 1
            storeService.removeStore(storeId);

            refundResult = reservationService.getRemoveStoreRefundPayment(storeId);

            if ( refundResult ) {

                log.info("goodBye User SUCCESS ! ");
                // 회원 상태값 변경
                userService.updateUserStatus(user);

                // 세션 삭제
                session.invalidate();
                return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
            } else {

                log.info("goodBye User FAIL ! ");
                return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
            }
        }

    }

}