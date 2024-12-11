package com.placeHere.server.controller.store;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.store.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Controller
public class StoreController {

    // Field
    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

    @Autowired
    @Qualifier("communityServiceImpl")
    private CommunityService communityService;

    @Autowired
    @Qualifier("likeServiceImpl")
    private LikeService likeService;

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService reservationService;

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

    @Value("${google_api}")
    private String googleApi;


    // Constructor
    public StoreController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }


    // Method
    // TODO 가게 등록 중간에 닫어버리면?
    // 가게 기본 정보 등록
    @GetMapping(value="/store/addStore")
    public String addStore(HttpSession session, Model model) {

        System.out.println("/store/addStore : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        // 로그인 안 한 경우
        if (user == null) {

            return "redirect:/user/login";
            
        // 점주 회원의 경우
        } else if (user.getRole().equals("ROLE_STORE")) {

            int storeId = storeService.getStoreId(user.getUsername());
            
            // 등록된 가게가 있는 경우
            if (storeId != 0) {

                Store store = storeService.getStore(storeId);

                // 등록된 가게 운영이 있는 경우
                if (store.getStoreOperation() != null) {

                    return "redirect:/store/getMyStore";
                }

                // 등록된 가게 운영이 없는 경우
                else {

                    return "redirect:/store/addOperation";
                }
                
            }
            
            // 등록된 가게가 없는 경우
            else {

                model.addAttribute("foodCategory", new FoodCategory());
                model.addAttribute("userName", user.getUsername());
                model.addAttribute("amenitiesNameList", amenitiesNameList);

                return "store/addStore";
            }
        
        // 일반 회원의 경우
        } else {

            return "redirect:/";
        }

    }

    @PostMapping("/store/addStore")
    public String addStore(@ModelAttribute Store store, Model model) {

        System.out.println("/store/addStore : POST");

        List<String> hashtagList = store.getHashtagList();
        hashtagList.removeIf(hashtag -> hashtag == null || hashtag.isEmpty());
        store.setHashtagList(hashtagList);

        int storeId = storeService.addStore(store);
        store.setStoreId(storeId);

        System.out.println(store);

        return "redirect:/store/addOperation";
    }


    // 가게 운영 정보 등록
    @GetMapping("/store/addOperation")
    public String addOperation(HttpSession session, Model model) {

        System.out.println("/store/addOperation : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        // 로그인 안 한 경우
        if (user == null) {

            return "redirect:/user/login";
        
        // 점주 회원의 경우
        } else if (user.getRole().equals("ROLE_STORE")) {

            int storeId = storeService.getStoreId(user.getUsername());
            Store store = storeService.getStore(storeId);
            
            // 등록된 가게가 있는 경우
            if (storeId != 0) {
                
                // 등록된 가게 운영이 없는 경우
                if (store.getStoreOperation() == null) {

                    model.addAttribute("userName", store.getUserName());
                    model.addAttribute("storeId", storeId);

                    return "store/addOperation";
                
                // 등록된 가게 운영이 있는 경우
                } else {

                    return "redirect:/store/getMyStore";
                }
            
            // 등록된 가게가 없는 경우
            } else {

                return "redirect:/store/addStore";
            }
        
        // 일반 회원의 경우
        } else {

            return "redirect:/";
        }
    }

    @PostMapping("/store/addOperation")
    public String addOperation(@ModelAttribute StoreOperation storeOperation, Model model) {

        System.out.println("/store/addOperation : POST");

        System.out.println(storeOperation);

        storeService.addOperation(storeOperation);

        return "redirect:/store/getMyStore";
    }


    // 가게 기본 정보 수정
    @GetMapping(value = "/store/updateStore")
    public String updateStore(HttpSession session, Model model) {

        System.out.println("/store/updateStore : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        // 로그인 안 한 경우
        if (user == null) {

            return "redirect:/user/login";
        }

        // 점주 회원의 경우
        else if (user.getRole().equals("ROLE_STORE")) {

            int storeId = storeService.getStoreId(user.getUsername());
            
            // 등록된 가게가 없는 경우
            if (storeId == 0) {

                return "redirect:/store/addStore";

            } else {

                Store store = storeService.getStore(storeId);

                if (store.getStoreOperation() == null) {

                    return "redirect:/store/addOperation";
                }

                else {

                    List<String> selectedCategoryList = Arrays.asList(store.getFoodCategoryId().split("/"));

                    System.out.println(store);
                    System.out.println(selectedCategoryList);

                    model.addAttribute("store", store);
                    model.addAttribute("selectedCategoryList", selectedCategoryList);
                    model.addAttribute("foodCategory", new FoodCategory());
                    model.addAttribute("amenitiesNameList", amenitiesNameList);

                    return "store/updateStore";
                }
            }

        }

        // 일반 회원의 경우
        else {

            return "redirect:/";
        }

    }

    @PostMapping("/store/updateStore")
    public String updateStore(@ModelAttribute Store store, Model model) {
        
        // TODO 사진 입력값이 없을 시 기존 사용하는 코드 추가 필요
        
        System.out.println("/store/updateStore : POST");

        List<String> hashtagList = store.getHashtagList();
        hashtagList.removeIf(hashtag -> hashtag == null || hashtag.isEmpty());
        store.setHashtagList(hashtagList);

        System.out.println("changeStore");
        System.out.println(store);

        Store beforeStore = storeService.getStore(store.getStoreId());
        boolean amenitiesEquals = store.amenitiesEquals(beforeStore.getAmenitiesNoList());
        boolean menuEquals = store.menuEquals(beforeStore.getMenuList());

        storeService.updateStore(store, amenitiesEquals, menuEquals);

        return "redirect:/store/updateOperation";
    }


    // 가게 운영 정보 수정
    @GetMapping("/store/updateOperation")
    public String updateOperation(HttpSession session, Model model) {

        System.out.println("/store/updateOperation : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        
        // 로그인 안 한 경우
        if (user == null) {

            return "redirect:/user/login";
        }
        
        // 점주 회원의 경우
        else if (user.getRole().equals("ROLE_STORE")) {

            int storeId = storeService.getStoreId(user.getUsername());

            if (storeId == 0) {

                return "redirect:/store/addStore";

            }

            else {

                Store store = storeService.getStore(storeId);

                StoreOperation storeOperation = storeService.getOperation(store.getStoreId());

                System.out.println(storeOperation);

                if (storeOperation == null) {

                    return "redirect:/store/addOperation";

                }

                else {

                    model.addAttribute("storeOperation", storeOperation);
                    model.addAttribute("userName", store.getUserName());
                    model.addAttribute("storeId", store.getStoreId());

                    return "store/updateOperation";
                }

            }

        }
        
        // 일반 회원의 경우
        else {

            return "redirect:/";
        }

    }

    @PostMapping("/store/updateOperation")
    public String updateOperation(@ModelAttribute StoreOperation storeOperation, Model model) {

        System.out.println("/store/updateOperation : POST");

        System.out.println(storeOperation);

        storeService.updateOperation(storeOperation);

        Store store = storeService.getStore(storeOperation.getStoreId());

        model.addAttribute("store", store);

        return "redirect:/store/getMyStore";
    }


    // 가게 검색
    @GetMapping("/searchStore")
    public String searchStore(HttpSession session, Model model) {

        System.out.println("/searchStore : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        model.addAttribute("mode", "search");
        model.addAttribute("regionList", regionList);
        model.addAttribute("foodCategory", new FoodCategory());
        model.addAttribute("amenitiesNameList", amenitiesNameList);
        model.addAttribute("search", new Search());

        return "store/searchStore";
    }


    // 가게 목록 조회
    @GetMapping("/getStoreList")
    public String getStoreList(@ModelAttribute Search search,
                               HttpSession session,
                               Model model) {

        System.out.println("/getStoreList : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

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

        // 구글 맵
        model.addAttribute("googleApi", googleApi);

        return "store/getStoreList";
    }


    // 가게 정보 조회
    @GetMapping(value = "/getStore", params = {"storeId"})
    public String getStore(@RequestParam("storeId") int storeId,
                           @RequestParam(value = "mode", required = false, defaultValue = "info") String mode,
                           @ModelAttribute Search search,
                           HttpSession session,
                           Model model) throws Exception {

        System.out.println("/getStore : GET");
        System.out.println(storeId);
        System.out.println("mode: " + mode);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        Store store = storeService.getStore(storeId, Date.valueOf(LocalDate.now()));
        search.setPageSize(pageSize);
        search.setListSize(listSize);

        System.out.println(store);

        if (store == null) {
            return "redirect:/";
        }

        else {

            // 회원의 좋아요 여부
            if (user != null && user.getRole().equals("ROLE_USER")) {

                Like like = new Like(user.getUsername());
                like.setRelationNo(storeId);
                like.setTarget("store");

                Like chkLike = likeService.chkLike(like);

                model.addAttribute("like", chkLike);
            }

            model.addAttribute("store", store);
            model.addAttribute("mode", mode);

            switch (mode) {

                case "info":
                    // 가게 정보
                    System.out.println("/getStore 가게 정보");

                    model.addAttribute("amenitiesNameList", amenitiesNameList);
                    model.addAttribute("googleApi", googleApi);

                    break;

                case "statistics":
                    // 예약 통계
                    System.out.println("/getStore 예약통계");
                    Map<String, Map<String, Integer>> statistics = storeService.getStatistics(storeId);

                    model.addAttribute("weekRsrv", statistics.get("cntWeekRsrv"));
                    model.addAttribute("rsrvAvg", statistics.get("cntRsrvAvg"));
                    model.addAttribute("percent", statistics.get("calcRsrvPercent"));

                    model.addAttribute("statistics", statistics);

                    break;

                case "review":
                    // 가게 리뷰
                    System.out.println("/getStore 가게 리뷰");
                    List<Review> reviewList = communityService.getReviewList(storeId, search);
                    int totalCnt = (reviewList != null && !reviewList.isEmpty()) ? reviewList.get(0).getReviewTotalCnt() : 0;

                    model.addAttribute("reviewList", reviewList);
                    model.addAttribute("totalCnt", totalCnt);

                    break;

                case "nearby":
                    // 가게 주변시설 추천
                    System.out.println("/getStore 주변시설 추천");

                    break;
            }

            return "store/getStore";
        }
    }


    // 가게 좋아요 목록 조회
    @GetMapping("/getStoreLikeList")
    public String getLikeStoreList(HttpSession session,
                                   Model model) throws Exception {

        System.out.println("/store/getLikeStoreList : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user != null) {

            if (user.getRole().equals("ROLE_USER")) {

                List<Like> storeLikeList = likeService.getStoreLikeList(user.getUsername());

                model.addAttribute("storeLikeList", storeLikeList);

                return "test/store/getStoreLikeListTest";

            }

            else {

                return "redirect:/";
            }
        }

        else {
            return "redirect:/user/login";
        }

    }


    // TODO 내 가게 리뷰보기, 매장 소식 보기, 휴무일 보기 합치기
    // 점주 회원 마이페이지 (가게 관리)
    @GetMapping("/store/getMyStore")
    public String getMyStore(@RequestParam(value = "mode", required = false, defaultValue = "review") String mode,
                             @ModelAttribute("search") Search search,
                             @ModelAttribute("message") String message,
                             HttpSession session,
                             Model model) {

        System.out.println("/store/getMyStore : GET");
        System.out.println("mode: " + mode);
        
        // TODO 로그인 중인 회원 아이디 가져오기, 역할 확인
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/user/login";
        }

        else if (user.getRole().equals("ROLE_STORE")) {

            search.setPageSize(pageSize);
            search.setListSize(listSize);

            int storeId = storeService.getStoreId(user.getUsername());

            if (storeId == 0) {

                return "redirect:/store/addStore";
            }

            else {

                Store store = storeService.getStore(storeId);

                if (store.getStoreOperation() == null) {

                    return "redirect:/store/addOperation";
                }

                else {

                    model.addAttribute("store", store);
                    model.addAttribute("mode", mode);

                    switch (mode) {

                        case "review":

                            // 내 가게 리뷰 목록 조회
                            System.out.println("내 가게 리뷰 목록 조회");

                            List<Review> reviewList = communityService.getReviewList(storeId, search);
                            int reviewTotalCnt = (reviewList != null && !reviewList.isEmpty()) ? reviewList.get(0).getReviewTotalCnt() : 0;

                            model.addAttribute("reviewList", reviewList);
                            model.addAttribute("totalCnt", reviewTotalCnt);

                            break;

                        case "news":

                            // 매장 소식 목록 조회
                            System.out.println("내 매장 소식 목록 조회");

                            List<StoreNews> storeNewsList = storeService.getStoreNewsList(storeId, search);
                            int newsTotalCnt = (storeNewsList.isEmpty()) ? 0 : storeNewsList.get(0).getTotalCnt();

                            model.addAttribute("storeNewsList", storeNewsList);
                            model.addAttribute("totalCnt", newsTotalCnt);

                            break;

                        case "closeday":

                            // 휴무일 목록 조회
                            System.out.println("내 가게 휴무일 목록 조회");

                            List<Closeday> closedayList = storeService.getClosedayList(storeId, search);
                            int closedayTotalCnt = (closedayList.isEmpty()) ? 0 : closedayList.get(0).getTotalCnt();

                            model.addAttribute("totalCnt", closedayTotalCnt);
                            model.addAttribute("closedayList", closedayList);
                            model.addAttribute("search", search);
                            model.addAttribute("today", LocalDate.now());
                            model.addAttribute("message", message);

                            break;

                    }

                    return "store/getMyStore";
                }

            }

        }

        else {

            return "redirect:/";
        }

    }

    @PostMapping("/store/getMyStore")
    public String getMyStore(@ModelAttribute StoreNews storeNews,
                             @RequestParam("mode") String mode,
                             @RequestParam(value = "fnc", required = false) String fnc,
                             @ModelAttribute Closeday closeday,
                             @ModelAttribute Search search,
                             RedirectAttributes redirectAttributes) throws Exception {

        System.out.println("/store/getMyStore : POST");
        System.out.println("mode: " + mode);
        System.out.println("fnc: " + fnc);

        switch (mode) {

            case "news":

                System.out.println(storeNews);

                // 매장 소식 등록, 수정, 삭제
                switch (fnc) {

                    case "add":
                        // 매장 소식 등록
                        System.out.println("addStoreNews");

                        storeService.addStoreNews(storeNews);

                        break;

                    case "update":
                        // 매장 소식 수정
                        System.out.println("updateStoreNews");

                        storeService.updateStoreNews(storeNews);

                        break;

                    case "remove":
                        // 매장 소식 삭제
                        System.out.println("removeStoreNews");

                        storeService.removeStoreNews(storeNews.getNewsId());

                        break;
                }

                break;

            case "closeday":

                if (fnc.equals("get")) {
                    System.out.println(search);
                } else {
                    System.out.println(closeday);
                }

                // 휴무일 등록, 검색, 삭제
                switch (fnc) {

                    case "add":
                        // 휴무일 등록
                        System.out.println("addCloseday");

                        // TODO 예약이 있는지 확인은 Rest로? 변경 생각해보기
                        int rsrvCnt = reservationService.getCountDayRsrv(Date.valueOf(closeday.getCloseday()), closeday.getStoreId());

                        System.out.println(rsrvCnt);

                        // 예약이 없으면 휴무일 추가, 있으면 등록 불가 메세지 전달
                        if (rsrvCnt == 0) {
                            storeService.addCloseday(closeday);
                        } else {
                            redirectAttributes.addFlashAttribute("message", "해당 날짜에 예약이 있어 휴무일 등록이 불가능합니다");
                        }

                        break;

                    case "get":
                        // 휴무일 목록 조회
                        System.out.println("getClosedayList");

                        search.setPageSize(pageSize);
                        search.setListSize(listSize);

                        redirectAttributes.addFlashAttribute("search", search);

                        break;

                    case "remove":
                        // 휴무일 삭제
                        System.out.println("removeCloseday");
                        System.out.println(closeday.getClosedayId());

                        storeService.removeCloseday(closeday.getClosedayId());

                        break;
                }

                break;
        }

        return "redirect:/store/getMyStore?mode=" + mode;
    }

}
