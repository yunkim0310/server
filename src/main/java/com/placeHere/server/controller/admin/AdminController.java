package com.placeHere.server.controller.admin;


import com.placeHere.server.domain.Batch;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.admin.AdminService;
import com.placeHere.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api-admin")
public class AdminController {

    @Autowired
    @Qualifier("adminServiceImpl")
    private AdminService adminService;
    

    // 회원 상세보기
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam(value="id") long id) throws Exception {

        log.info("id :: " + id);

        log.info("http://localhost:8080/api-admin/getUser - GET Controller ");

        User user = adminService.getUser(id);

        if ( user != null ) {
            log.info(" >>> 가져온 user :: " + user );
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    // 회원 리스트 목록
    @GetMapping("/getUserList")
    public ResponseEntity<?> getUserList() throws Exception {

        log.info("/api-admin/getUserList - GET Controller ");

        List<User> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<String, Object>();

        list = adminService.getUserList();

        int total = list.size();

        for(User user : list) {
            System.out.println(user);
        }

        map.put("data", list);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (list != null) {

            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
            String contentRange = "getUserList 0-" + (list.size() - 1) + "/" + list.size();

            headers.add("Access-Control-Expose-Headers", "Content-Range"); // CORS 관련 헤더 노출

            log.info("Content-Range: " + contentRange);

            // list 에서 map으로 변경
//            return new ResponseEntity<>(list, headers, HttpStatus.OK);
            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getStoreList")
    public ResponseEntity<?> getStoreList() throws Exception {

        log.info("/api-admin/getStoreList - GET Controller ");
        List<User> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 점주회원 리스트
        list = adminService.getStoreList();

        // 점주회원 개수
        int total = list.size();

//        for(User user : list) {
//            System.out.println(user);
//        }

        map.put("data", list);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (list != null) {

            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
            String contentRange = "getUserList 0-" + (list.size() - 1) + "/" + list.size();

            headers.add("Access-Control-Expose-Headers", "Content-Range"); // CORS 관련 헤더 노출

            log.info("Content-Range: " + contentRange);

            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {

            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }


    // 예약 리스트
    @GetMapping("/getRsrvList")
    public ResponseEntity<?> getRsrvList() throws Exception {

        log.info("/api-admin/getRsrvList - GET Controller ");
        List<Reservation> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 점주회원 리스트
        list = adminService.getRsrvList();

        // 점주회원 개수
        int total = list.size();

//        for(Reservation rsrv : list) {
//            System.out.println(rsrv);
//        }

        map.put("data", list);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();


        if (list != null) {

            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
            String contentRange = "getRsrvList 0-" + (list.size() - 1) + "/" + list.size();

            headers.add("Access-Control-Expose-Headers", "Content-Range"); // CORS 관련 헤더 노출

            log.info("Content-Range: " + contentRange);

            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {

            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getRsrv")
    public ResponseEntity<?> getRsrv(@RequestParam(value="rsrvNo") int id) throws Exception {

        log.info("id :: " + id);

        log.info("http://localhost:8080/api-admin/getRsrv - GET Controller ");

        Reservation rsrv = adminService.getRsrv(id);

        if ( rsrv != null ) {
            log.info(" >>> 가져온 user :: " + rsrv );
            return new ResponseEntity<>(rsrv, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBatchList")
    public ResponseEntity<?> getBatchList() throws Exception {

        log.info("http://localhost:8080/api-admin/getBatchList - GET Controller ");

        List<Batch> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 배치 리스트
        list = adminService.getBatchList();

        // 개수
        int total = list.size();

        map.put("data", list);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (list != null) {

            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
            String contentRange = "getRsrvList 0-" + (list.size() - 1) + "/" + list.size();

            headers.add("Access-Control-Expose-Headers", "Content-Range"); // CORS 관련 헤더 노출

            log.info("Content-Range: " + contentRange);

            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {

            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }

}
