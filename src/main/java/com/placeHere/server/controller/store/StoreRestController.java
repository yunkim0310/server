package com.placeHere.server.controller.store;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.aws.AwsS3Service;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.store.SearchService;
import com.placeHere.server.service.store.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api-store/*")
public class StoreRestController {

    // Field
    @Autowired
    private StoreService storeService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private SearchService searchService;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    @Value("${business_no_api}")
    private String businessNoApiKey;

    @Value("${google_api}")
    private String googleApiKey;


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
    @GetMapping(value = "/addStoreLike", params = {"relationNo"})
    public ResponseEntity<Integer> addLikeStore(HttpSession session,
                                             @ModelAttribute Like like) throws Exception {

        System.out.println("/api-store/addLikeStore/");

        User user = (User) session.getAttribute("user");

        if (user == null) {

            return ResponseEntity.ok(0);
        }

        else {

            like.setUserName(user.getUsername());
            like.setTarget("store");

            System.out.println(like);

            likeService.addLike(like.getUserName(), like.getRelationNo(), like.getTarget());

            Like result = likeService.chkLike(like);

            return ResponseEntity.ok(result.getLikeId());
        }
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
    public ResponseEntity<List<Store>> getStoreList(@ModelAttribute Search search) {

        System.out.println("/api-store/getStoreList : GET");
        System.out.println("search : " + ((search != null) ? search.getSearchKeyword() : "null"));

        List<Store> storeList = storeService.getStoreList(search);

        System.out.println("storeList : " + ((storeList != null) ? storeList.size() : "null"));

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


    // API Key 전달
    @GetMapping("/getApiKey")
    public ResponseEntity<Map<String, String>> getApiKey() {

        System.out.println("/api-store/getApiKey : GET");

        Map<String, String> response = new HashMap<>();
        response.put("businessNo", businessNoApiKey);
        response.put("google", googleApiKey);

        return ResponseEntity.ok(response);
    }


    // 가게 위치 전달
    @GetMapping("/getStoreLocation")
    public ResponseEntity<List<Map<String,String>>> getStoreLocation(@ModelAttribute Search search,
                                                                     @RequestParam(value = "storeId", required = false, defaultValue = "0") int storeId) {
        System.out.println("/api-store/getStoreLocation : GET");

        if (storeId == 0) {

            // 필터에서 선택한 음식 카테고리
            List<String> selectedCategoryList = Arrays.asList(search.getFoodCategoryId().split("/"));

            if (selectedCategoryList.get(0).equals("")) {
                selectedCategoryList = List.of("","","");
            } else if (selectedCategoryList.size() == 1) {
                selectedCategoryList = List.of(selectedCategoryList.get(0), "전체", "전체");
            }

            // 선택한 음식 카테고리 변경 (전체, 기타 제거) - 검색을 위해서
            String foodCategoryId = search.getFoodCategoryId();
            foodCategoryId = foodCategoryId.replace("전체/", "").replace("기타/", "");
            search.setFoodCategoryId(foodCategoryId);
            System.out.println(search);

            // 입력값없는 해시태그 제거
            List<String> hashtagList = search.getHashtagList();

            if (hashtagList != null && !hashtagList.isEmpty()) {
                hashtagList.removeIf(hashtag -> hashtag == null || hashtag.isEmpty());
                search.setHashtagList(hashtagList);
            }

            List<Map<String,String>> storeLocationList = storeService.getStoreLocationList(search);
            System.out.println(storeLocationList);

            return ResponseEntity.ok(storeLocationList);

        } else {

            List<Map<String, String>>storeLocation = storeService.getStoreLocation(storeId);
            System.out.println(storeLocation);

            return ResponseEntity.ok(storeLocation);
        }

    }


    // 예약 통계 데이터 전달
    @GetMapping(value = "/getStatistics", params = "storeId")
    public ResponseEntity<Map<String,Map<String, Integer>>> getStatistics(@RequestParam("storeId") int storeId) {

        System.out.println("/api-store/getStatistics : GET");

        Map<String, Map<String, Integer>> statistics = storeService.getStatistics(storeId);

        return ResponseEntity.ok(statistics);
    }


    // 사진 삭제
    @GetMapping("/removeFile")
    public void removeFile(@RequestParam("filePath") String filePath) {

        awsS3Service.deleteFile(filePath);

    }


    // 월별 휴무일 전달
    @GetMapping("/getClosedayByMonth")
    public ResponseEntity<List<Closeday>> getClosedayByMonth(@ModelAttribute Search search,
                                                           @RequestParam int storeId) {

        System.out.println("/api-store/getClosedayByMonth : GET");

        List<Closeday> closedayList = storeService.getClosedayList(storeId, search);
        System.out.println(closedayList);

        return ResponseEntity.ok(closedayList);
    }


    // 휴무일 삭제
    @PostMapping("/removeCloseday")
    public ResponseEntity<Void> removeCloseday(@RequestParam int closedayId) {

        System.out.println("/api-store/removeCloseday : POST");

        boolean result = storeService.removeCloseday(closedayId);

        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // 검색어 등록
    @PostMapping("/addSearchKeyword")
    public ResponseEntity<Void> addSearchKeyword(@ModelAttribute Search search) {

        System.out.println("/api-store/addSearchKeyword : POST");

        boolean result = false;

        // 검색어가 있을 경우 검색 기록 등록
        if (search.getSearchKeyword() != null && !search.getSearchKeyword().trim().isEmpty()) {
            result = searchService.addSearch(search.getSearchKeyword());
        }

        if (result) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
