package com.placeHere.server.controller.store;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class StoreController {

    // Field
    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

    @Value("${business_no_api}")
    private String apiKey;

    @Value("${store_upload_dir}")
    private String uploadDir;

    @Value("${amenities_name_list}")
    private List<String> amenitiesNameList;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    @Value("${region_list}")
    private List<String> regionList;


    // Constructor
    public StoreController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }


    // Method
    // 가게 기본 정보 등록
    @GetMapping(value="/store/addStore", params = "userName")
    public String addStore(@RequestParam("userName") String userName, Model model) {

        System.out.println("/store/addStore : GET");

        if (storeService.getStoreId(userName) != 0) {

            // 이미 등록된 가게가 있을시 어디로 보낼지 고민 TODO
            return null;
        }

        else {

            System.out.println(userName);

            model.addAttribute("foodCategory", new FoodCategory());
            model.addAttribute("userName", userName);
            model.addAttribute("apiKey", apiKey);
            model.addAttribute("amenitiesNameList", amenitiesNameList);

            return "test/store/addStoreTest";
        }
    }

    @PostMapping("/store/addStore")
    public String addStore(@ModelAttribute Store store, Model model) {

        System.out.println("/store/addStore : POST");

        System.out.println(store);

        int storeId = storeService.addStore(store);

        model.addAttribute("userName", store.getUserName());
        model.addAttribute("storeId", storeId);

        return "test/store/addOperationTest";
    }


    // 가게 운영 정보 등록
    @PostMapping("/store/addOperation")
    public String addOperation(@ModelAttribute StoreOperation storeOperation, Model model) {

        System.out.println("/store/addOperation : POST");

        System.out.println(storeOperation);

        storeService.addOperation(storeOperation);

        Store store = storeService.getStore(storeOperation.getStoreId());

        model.addAttribute("store", store);

        return "test/store/addStoreTestResult";
    }


    // 가게 기본 정보 수정
    @GetMapping(value = "/store/updateStore", params = "userName")
    public String updateStore(@RequestParam("userName") String userName, Model model) {

        System.out.println("/store/updateStore : GET");

        int storeId = storeService.getStoreId(userName);

        if (storeId == 0) {

            return null;
        }

        else {

            Store store = storeService.getStore(storeId);
            List<String> selectedCategoryList = Arrays.asList(store.getFoodCategoryId().split("/"));

            System.out.println(store);
            System.out.println(selectedCategoryList);

            model.addAttribute("store", store);
            model.addAttribute("selectedCategoryList", selectedCategoryList);
            model.addAttribute("foodCategory", new FoodCategory());
            model.addAttribute("amenitiesNameList", amenitiesNameList);

            return "test/store/updateStoreTest";
        }

    }

    @PostMapping("/store/updateStore")
    public String updateStore(@ModelAttribute Store store, Model model) {

        System.out.println("/store/updateStore : POST");

        System.out.println(store);

        storeService.updateStore(store);

        StoreOperation storeOperation = storeService.getOperation(store.getStoreId());

        System.out.println(storeOperation);

        model.addAttribute("storeOperation", storeOperation);
        model.addAttribute("userName", store.getUserName());
        model.addAttribute("storeId", store.getStoreId());

        return "test/store/updateOperationTest";
    }


    // 가게 운영 정보 수정
    @PostMapping("/store/updateOperation")
    public String updateOperation(@ModelAttribute StoreOperation storeOperation, Model model) {

        System.out.println("/store/updateOperation : POST");

        System.out.println(storeOperation);

        storeService.updateOperation(storeOperation);

        Store store = storeService.getStore(storeOperation.getStoreId());

        model.addAttribute("store", store);

        return "test/store/updateStoreTestResult";
    }


    // 매장 소식 목록 조회
    @GetMapping(value = "/store/getStoreNewsList", params = "userName")
    public String getStoreNewsList(@RequestParam("userName") String userName, Model model) {

        System.out.println("/store/getStoreNewsList : GET");

        int storeId = storeService.getStoreId(userName);

        // 가게 미등록시
        if (storeId == 0) {
            return null;
        }

        else {
            Search search = new Search(pageSize, listSize);

            List<StoreNews> storeNewsList = storeService.getStoreNewsList(storeId, search);
            int totalCnt = (storeNewsList.isEmpty()) ? 0 : storeNewsList.get(0).getTotalCnt();

            model.addAttribute("userName", userName);
            model.addAttribute("storeId", storeId);
            model.addAttribute("storeNewsList", storeNewsList);
            model.addAttribute("totalCnt", totalCnt);

            return "test/store/getStoreNewsListTest";
        }

    }

    // 매장 소식 목록 추가, 수정, 삭제
    @PostMapping("/store/getStoreNewsList")
    public String getStoreNewsList(@ModelAttribute StoreNews storeNews,
                                   @RequestParam("mode") String mode,
                                   @RequestParam("userName") String userName,
                                   Model model) {

        System.out.println("/store/getStoreNewsList : POST");

        System.out.println(mode);
        System.out.println(storeNews);

        switch (mode) {

            case "add":
                // 매장 소식 등록
                System.out.println("addStoreNews");
                System.out.println(storeNews.getNewsId());
                storeService.addStoreNews(storeNews);
                break;

            case "update":
                // 매장 소식 수정
                System.out.println("updateStoreNews");
                System.out.println(storeNews.getNewsId());
                storeService.updateStoreNews(storeNews);
                break;

            case "remove":
                // 매장 소식 삭제
                System.out.println("removeStoreNews");
                System.out.println(storeNews.getNewsId());
                storeService.removeStoreNews(storeNews.getNewsId());
                break;

        }

        return "redirect:/store/getStoreNewsList?userName=" + userName;
    }


    // 휴무일 목록 조회
    @GetMapping(value = "/store/getClosedayList", params = "userName")
    public String getClosedayList(@RequestParam("userName") String userName,
                                  @ModelAttribute("search") Search search,
                                  Model model) {

        System.out.println("/store/getClosedayList : GET");

        int storeId = storeService.getStoreId(userName);

        // 가게 미등록시
        if (storeId == 0) {
            return null;
        }

        else {

            search.setPageSize(pageSize);
            search.setListSize(listSize);
            System.out.println(search);

            List<Closeday> closedayList = storeService.getClosedayList(storeId, search);
            int totalCnt = (closedayList.isEmpty()) ? 0 : closedayList.get(0).getTotalCnt();

            model.addAttribute("userName", userName);
            model.addAttribute("storeId", storeId);
            model.addAttribute("totalCnt", totalCnt);
            model.addAttribute("closedayList", closedayList);
            model.addAttribute("search", search);
            model.addAttribute("today", LocalDate.now().toString());

            return "test/store/getClosedayListTest";
        }

    }

    // 휴무일 추가, 삭제
    @PostMapping(value = "/store/getClosedayList", params = "userName")
    public String getClosedayList(@ModelAttribute Closeday closeday,
                                  @ModelAttribute Search search,
                                  @RequestParam("userName") String userName,
                                  @RequestParam("mode") String mode,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        System.out.println("/store/getClosedayList : POST");

        System.out.println(mode);
        System.out.println(closeday);
        if (mode.equals("get")) {
            System.out.println(search);
        }

        switch (mode) {

            case "add":
                // 휴무일 등록
                System.out.println("addCloseday");
                storeService.addCloseday(closeday);
                break;

            case "get":
                // 휴무일 목록 조회
                System.out.println("getClosedayList");

                search.setPageSize(pageSize);
                search.setListSize(listSize);

                redirectAttributes.addFlashAttribute("search", search);
                redirectAttributes.addFlashAttribute("userName", userName);
                break;

            case "remove":
                // 휴무일 삭제
                System.out.println("removeCloseday");
                System.out.println(closeday.getClosedayId());
                storeService.removeCloseday(closeday.getClosedayId());
                break;
        }

        return "redirect:/store/getClosedayList?userName=" + userName;
    }

    // 가게 검색
    @GetMapping("/searchStore")
    public String searchStore(Model model) {

        System.out.println("/searchStore : GET");

        model.addAttribute("mode", "search");
        model.addAttribute("regionList", regionList);
        model.addAttribute("foodCategory", new FoodCategory());
        model.addAttribute("amenitiesNameList", amenitiesNameList);

        return "test/store/searchStoreTest";
    }

    @PostMapping("/searchStore")
    public String searchStore(@ModelAttribute Search search,
                              Model model) {

        System.out.println("/searchStore : POST");

        System.out.println(search);


        return "test/store/getStoreListTest";
    }


    // 가게 목록 조회
    @GetMapping("/getStoreList")
    public String getStoreList(@ModelAttribute Search search,
                               Model model) {

        System.out.println("/getStoreList : GET");

        search.setPageSize(pageSize);
        search.setListSize(listSize);

        List<String> selectedCategoryList = Arrays.asList(search.getFoodCategoryId().split("/"));

        if (selectedCategoryList.get(0).equals("")) {
            selectedCategoryList = List.of("","","");
        }

        List<String> hashtagList = search.getHashtagList();

        hashtagList.removeIf(hashtag -> hashtag == null || hashtag.isEmpty());

        search.setHashtagList(hashtagList);

        model.addAttribute("mode", "result");
        model.addAttribute("search", search);
        model.addAttribute("selectedCategoryList", selectedCategoryList);
        
        // 가게 목록 검색
        String foodCategoryId = search.getFoodCategoryId();
        foodCategoryId = foodCategoryId.replace("전체/", "").replace("기타/", "");
        search.setFoodCategoryId(foodCategoryId);
        System.out.println(search);

        List<Store> storeList = storeService.getStoreList(search);
        int totalCnt = (storeList.isEmpty()) ? 0 : storeList.get(0).getTotalCnt();

        System.out.println(storeList);

        // 가게 목록
        model.addAttribute("storeList", storeList);
        model.addAttribute("totalCnt", totalCnt);

        // 필터 관련
        model.addAttribute("regionList", regionList);
        model.addAttribute("foodCategory", new FoodCategory());
        model.addAttribute("amenitiesNameList", amenitiesNameList);

        return "test/store/getStoreListTest";
    }


    // 가게 정보 조회
    @GetMapping(value = "/getStore", params = "storeId")
    public String getStore(@RequestParam("storeId") int storeId, Model model) {

        System.out.println("/getStore : GET");

        Store store = storeService.getStore(storeId);

        System.out.println(store);

        model.addAttribute("store", store);

        return "test/store/getStoreTest";
    }
}
