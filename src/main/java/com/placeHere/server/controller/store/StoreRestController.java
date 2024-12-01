package com.placeHere.server.controller.store;

import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-store/*")
public class StoreRestController {

    // Field
    @Autowired
    private StoreService storeService;


    // Constructor
    public StoreRestController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }


    // Method
    // 사업자 번호 중복 확인
    @GetMapping("/chkDuplicateBusinessNo")
    public ResponseEntity<Boolean> chkDuplicateBusinessNo(@RequestParam("businessNo") String businessNo) {

        System.out.println("/api-store/chkDuplicateBusinessNo/" + businessNo);

        int count = storeService.chkDuplicateBusinessNo(businessNo);

        // 중복된 사업자번호가 있을 경우 false, 없을 경우 true
        return ResponseEntity.ok(count <= 0);
    }


    // 가게 좋아요 추가
    @GetMapping("/addLikeStore")
    public ResponseEntity<Boolean> addLikeStore(@RequestParam("storeId") int storeId) {

        System.out.println("/api-store/addLikeStore/" + storeId);

        return null;
    }


    // 가게 좋아요 취소
    @GetMapping("/removeLikeStore")
    public ResponseEntity<Boolean> removeLikeStore(@RequestParam("storeId") int storeId) {

        System.out.println("/api-store/removeLikeStore/" + storeId);

        return null;
    }

}
