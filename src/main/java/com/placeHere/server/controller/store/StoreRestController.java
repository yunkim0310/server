package com.placeHere.server.controller.store;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api-store/*")
public class StoreRestController {

    // Field
    @Autowired
    private StoreService storeService;

    @Autowired
    private LikeService likeService;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;




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
    @GetMapping(value = "/addStoreLike", params = {"userName", "relationNo", "target"})
    public ResponseEntity<Like> addLikeStore(@ModelAttribute Like like) throws Exception {

        System.out.println("/api-store/addLikeStore/");
        System.out.println(like);

        likeService.addLike(like.getUserName(), like.getRelationNo(), like.getTarget());

        Like result = likeService.chkLike(like);

        return ResponseEntity.ok(result);
    }


    // 가게 좋아요 취소
    @GetMapping(value = "/removeStoreLike", params = "likeId")
    public ResponseEntity<Boolean> removeLikeStore(@ModelAttribute Like like) throws Exception {

        System.out.println("/api-store/removeLikeStore/");
        System.out.println(like);

        boolean result = likeService.removeLike(like);

        return ResponseEntity.ok(result);
    }

    
    // 가게 목록 조회
    @GetMapping("/getStoreList")
    public ResponseEntity<List<Store>> getStoreList(@ModelAttribute Search search,
                               Model model) {

        System.out.println("/api-store/getStoreList : GET");

        List<Store> storeList = storeService.getStoreList(search);

        System.out.println(storeList);

        return ResponseEntity.ok(storeList);
    }


    // 가게 정보 조회
    @GetMapping(value = "/getStore", params = {"storeId"})
    public ResponseEntity<Store> getStore(@RequestParam("storeId") int storeId) {

        System.out.println("/api-store/getStore : GET");
        System.out.println(storeId);

        Store store = storeService.getStore(storeId);

        System.out.println(store);

        return ResponseEntity.ok(store);
    }

}
