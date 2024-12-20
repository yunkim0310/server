package com.placeHere.server.controller.admin;


import com.placeHere.server.domain.Batch;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.admin.AdminService;
import com.placeHere.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;

    // 회원 상세보기
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam(value="id") long id) throws Exception {

        log.info("id :: " + id);

        log.info("http://localhost:8080/api-admin/getUser - GET Controller ");

        User user = adminService.getUser(id);

        String profileImg = user.getProfileImg();
        profileImg = bucketUrl+"user/" + profileImg;
        user.setProfileImg(profileImg);

        if ( user != null ) {
            log.info(" >>> 가져온 user :: " + user );

            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
    }

    // 회원 리스트 목록
    @GetMapping("/getUserList")
    public ResponseEntity<?> getUserList(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int perPage,
                                         @RequestParam(required = false) String username
                                                                                            ) throws Exception {

        log.info("/api-admin/getUserList - GET Controller ");

        List<User> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<String, Object>();

        list = adminService.getUserList();

        // 검색 조건에 맞는 데이터 필터링
        if (username != null && !username.isEmpty()) {
            list = list.stream().filter(user -> user.getUsername().contains(username)).collect(Collectors.toList());
        }

        int total = list.size();
        int start = (page-1) * perPage;
        int end = Math.min(start + perPage, total);

        log.info("username : "+ username);
        log.info("page : "+ page);
        log.info("perPage : "+ perPage);
        log.info("start : "+ start);
        log.info("end : "+ end);

        List<User> pagedList = list.subList(start, end);

        for(User user : pagedList) {
            System.out.println(user);
        }

        // 페이징 처리된 list put
        map.put("data", pagedList);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (pagedList != null) {

//            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
//            String contentRange = "getUserList 0-" + (list.size() - 1) + "/" + list.size();
//            headers.add("Access-Control-Expose-Headers", "Content-Range");
//            log.info("Content-Range: " + contentRange);

            headers.add("Content-Range", "items " + start + "-" + (end - 1) + "/" + total);
            headers.add("Access-Control-Expose-Headers", "Content-Range");

            log.info("Content-Range: " + "items " + start + "-" + (end - 1) + "/" + total);

            // list 에서 map으로 변경
            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getStoreList")
    public ResponseEntity<?> getStoreList(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int perPage,
                                          @RequestParam(required = false) String username
                                                                                            ) throws Exception {

        log.info("/api-admin/getStoreList - GET Controller ");
        List<User> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 점주회원 리스트
        list = adminService.getStoreList();

        if (username != null && !username.isEmpty()) {
            list = list.stream().filter(user -> user.getUsername().contains(username)).collect(Collectors.toList());
        }

        // 점주회원 개수
        int total = list.size();
        int start = (page-1) * perPage;
        int end = Math.min(start + perPage, total);

        log.info("username : "+ username);
        log.info("page : "+ page);
        log.info("perPage : "+ perPage);
        log.info("start : "+ start);
        log.info("end : "+ end);

        // 페이징된 리스트
        List<User> pagedList = list.subList(start, end);

        for(User user : list) {
            System.out.println(user);
        }

        map.put("data", pagedList);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (list != null) {
//            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
//            String contentRange = "getStoreList 0-" + (list.size() - 1) + "/" + list.size();
//            headers.add("Access-Control-Expose-Headers", "Content-Range"); // CORS 관련 헤더 노출
//            log.info("Content-Range: " + contentRange);

            headers.add("Content-Range", "items " + start + "-" + (end - 1) + "/" + total);
            headers.add("Access-Control-Expose-Headers", "Content-Range");

            log.info("Content-Range: " + "items " + start + "-" + (end - 1) + "/" + total);

            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }


    // 예약 리스트
    @GetMapping("/getRsrvList")
    public ResponseEntity<?> getRsrvList(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int perPage,
                                         @RequestParam(required = false) String username
                                                                                        ) throws Exception {

        log.info("/api-admin/getRsrvList - GET Controller ");
        List<Reservation> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 예약 리스트
        list = adminService.getRsrvList();

        if (username != null && !username.isEmpty()) {
            list = list.stream()
                    .filter(reservation -> reservation.getUserName() != null && reservation.getUserName().contains(username))
                    .collect(Collectors.toList());
        }

        // 점주회원 개수
        int total = list.size();
        int start = (page-1) * perPage;
//        int start = page * perPage;
        int end = Math.min(start + perPage, total);

        log.info("page : "+ page);
        log.info("perPage : "+ perPage);
        log.info("start : "+ start);
        log.info("end : "+ end);

        List<Reservation> pagedList = list.subList(start, end);

        for(Reservation rsrv : pagedList) {
            System.out.println(rsrv);
        }

        map.put("data", pagedList);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (list != null) {

//            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
//            headers.add("Access-Control-Expose-Headers", "Content-Range"); // CORS 관련 헤더 노출
//            String contentRange = "getRsrvList 0-" + (list.size() - 1) + "/" + list.size();

            headers.add("Content-Range", "items " + start + "-" + (end - 1) + "/" + total);
            // CORS 관련 헤더 노출
            headers.add("Access-Control-Expose-Headers", "Content-Range");

            log.info("Content-Range: " + "items " + start + "-" + (end - 1) + "/" + total);
//            Content-Range: items 0-9/1750
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
    public ResponseEntity<?> getBatchList(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "10") int perPage,
                                          @RequestParam(required = false) String batchName
                                                                                            ) throws Exception {

        log.info("http://localhost:8080/api-admin/getBatchList - GET Controller ");

        List<Batch> list = new ArrayList<>();
        // 응답 데이터
        Map<String, Object> map = new HashMap<String, Object>();

        // 배치 리스트
        list = adminService.getBatchList();

        if (batchName != null && !batchName.isEmpty()) {
            list = list.stream()
                    .filter(batch -> batch.getBatchName() != null && batch.getBatchName().contains(batchName))
                    .collect(Collectors.toList());
        }

        // 개수
        int total = list.size();
        int start = (page-1) * perPage;
        int end = Math.min(start + perPage, total);

        log.info("page : "+ page);
        log.info("perPage : "+ perPage);
        log.info("start : "+ start);
        log.info("end : "+ end);

        // 페이징된 리스트
        List<Batch> pagedList = list.subList(start, end);

        for(Batch batch : pagedList) {
            log.info("batchId :: " + batch.getId().toString());
            log.info("batchName :: " + batch.getBatchName());

        }
//        map.put("data", list);
        map.put("data", pagedList);
        map.put("total", total);

        HttpHeaders headers = new HttpHeaders();

        if (list != null) {

//            headers.add("Content-Range", "getUserList 0-" + (list.size() - 1) + "/" + list.size());
//            String contentRange = "getRsrvList 0-" + (list.size() - 1) + "/" + list.size();
//            log.info("Content-Range: " + contentRange);
            headers.add("Content-Range", "items " + start + "-" + (end - 1) + "/" + total);
            // CORS 관련 헤더 노출
            headers.add("Access-Control-Expose-Headers", "Content-Range");

            log.info("Content-Range: " + "items " + start + "-" + (end - 1) + "/" + total);

            return new ResponseEntity<>(map, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("FAIL", headers, HttpStatus.BAD_REQUEST);
        }

    }

}
