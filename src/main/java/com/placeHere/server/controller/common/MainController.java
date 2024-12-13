package com.placeHere.server.controller.common;

import com.placeHere.server.domain.Store;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.store.StoreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    // Field
    @Autowired
    StoreService storeService;

    @Autowired
    LikeService likeService;

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
        
        // 인기 가게
        List<Integer> storeIdList =  likeService.likeList("store");
        List<Store> storeList = new ArrayList<Store>();

        if (!storeIdList.isEmpty()) {

            storeList = storeService.getStoreList(storeIdList);

        } 

        System.out.println(storeIdList);
        System.out.println(storeList);

        model.addAttribute("user", user);
        model.addAttribute("storeList", storeList);

        return "index";
    }
}
