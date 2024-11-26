package com.placeHere.server.controller.store;

import com.placeHere.server.domain.Store;
import com.placeHere.server.domain.StoreOperation;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/store/*")
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


    // Constructor
    public StoreController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }


    // Method
    // 가게 기본 정보 등록
    @GetMapping("/addStore")
    public String addStore(Model model) {

        System.out.println("/store/addStore : GET");

        model.addAttribute("userName", "user01");
        model.addAttribute("apiKey", apiKey);
        model.addAttribute("amenitiesNameList", amenitiesNameList);

        return "/test/store/addStoreTest";
    }

    @PostMapping("/addStore")
    public String addStore(@ModelAttribute Store store, Model model) {

        System.out.println("/store/addStore : POST");

        model.addAttribute("userName", "user01");
        model.addAttribute("storeId", 4);

        System.out.println(store);

        return "/test/store/addOperationTest";
    }

    @PostMapping("/addOperation")
    public String addOperation(@ModelAttribute StoreOperation storeOperation) {

        System.out.println("/store/addOperation : POST");

        System.out.println(storeOperation);

        return "redirect:/test/test";
    }
}
