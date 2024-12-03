package com.placeHere.server.controller.store;

import com.placeHere.server.domain.Like;
import com.placeHere.server.service.like.LikeService;
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

    @Autowired
    private LikeService likeService;


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

}
