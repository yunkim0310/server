package com.placeHere.server.controller.common;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.store.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class MainController {

    // Field
    @Autowired
    private StoreService storeService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private LikeService likeService;

    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;

    @Value("${region_list}")
    private List<String> regionList;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;


    // Constructor
    public MainController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }


    // Method
    @GetMapping("/")
    public String index(HttpSession session, Model model) throws Exception {

        System.out.println("Home : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        // 메인 가게 사진
        List<Store> mainStoreList = storeService.getStoreList(new Search(pageSize, 5));
        model.addAttribute("mainStoreList", mainStoreList);

        // 인기 가게
        List<Integer> storeIdList = likeService.likeList("store");
        List<Store> storeList = new ArrayList<Store>();

        if (!storeIdList.isEmpty()) {
            storeList = storeService.getStoreList(storeIdList);
        }

        System.out.println(storeIdList);
        System.out.println(storeList);

        System.out.println(storeList.isEmpty());

        // 지역 필터 이미지 TODO 수정 필요
        List<String> regionImgList = new ArrayList<String>(
                List.of("https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706apgujeong.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706itaewon.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706gangnam.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706hongdae.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706yeouido.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/busan_.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706sungsoo.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706bukchon_0331.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md_2022/0609_location_hapjeong,mangwon.png?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706euljiro_0331.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/jeju_.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706surraevillage_0331.jpg?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md_2022/0307_location_daegu@2x.png?small200",
                        "https://d21sjc85fy47a6.cloudfront.net/aaaaaqx/md/0706gwanghwamun.jpg?small200"));

        // 인기 리뷰
        List<Integer> reviewNoList = likeService.likeList("review");
        List<Review> reviewList = new ArrayList<Review>();

        if (reviewNoList != null && !reviewNoList.isEmpty()) {
            reviewList = communityService.getReviewListByReviewNo(reviewNoList);
        }

        System.out.println(reviewNoList);
        System.out.println(reviewList);

        // 월, 주차 계산
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int week = today.get(weekFields.weekOfMonth());

        // 음식 카테고리
        FoodCategory foodCategory = new FoodCategory();

        // 음식 카테고리, 지역 필터 TODO 음식,지역 이미지 리스트도 만들기
        model.addAttribute("foodCategory", foodCategory);
        model.addAttribute("regionList", regionList);
        model.addAttribute("regionImgList", regionImgList);

        // 버킷 url, 인기 가게, 인기 리뷰
        model.addAttribute("url", bucketUrl);
        model.addAttribute("storeList", storeList);
        model.addAttribute("reviewList", reviewList);

        // 현재 월, 주차
        model.addAttribute("month", month);
        model.addAttribute("week", week);

        return "index";
    }
}

