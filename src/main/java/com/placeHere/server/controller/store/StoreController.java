package com.placeHere.server.controller.store;

import com.placeHere.server.domain.FoodCategory;
import com.placeHere.server.domain.Store;
import com.placeHere.server.domain.StoreOperation;
import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value="/addStore", params = "userName")
    public String addStore(@RequestParam("userName") String userName, Model model) {

        System.out.println("/store/addStore : GET");

        if (storeService.getStoreId(userName) != 0) {

            // 이미 등록된 가게가 있을시 어디로 보낼지 고민 TODO
            return null;
        }

        else {

            model.addAttribute("foodCategory", new FoodCategory());
            model.addAttribute("userName", userName);
            model.addAttribute("apiKey", apiKey);
            model.addAttribute("amenitiesNameList", amenitiesNameList);

            return "/test/store/addStoreTest";
        }
    }

    @PostMapping("/addStore")
    public String addStore(@ModelAttribute Store store, Model model) {

        System.out.println("/store/addStore : POST");

        System.out.println(store);

        int storeId = storeService.addStore(store);

        model.addAttribute("userName", store.getUserName());
        model.addAttribute("storeId", storeId);

        return "/test/store/addOperationTest";
    }


    // 가게 운영 정보 등록
    @PostMapping("/addOperation")
    public String addOperation(@ModelAttribute StoreOperation storeOperation, Model model) {

        System.out.println("/store/addOperation : POST");

        System.out.println(storeOperation);

        storeService.addOperation(storeOperation);

        Store store = storeService.getStore(storeOperation.getStoreId());

        model.addAttribute("store", store);

        return "/test/store/addStoreTestResult";
    }


    // 가게 기본 정보 수정
    @GetMapping(value = "/updateStore", params = "userName")
    public String updateStore(@RequestParam("userName") String userName, Model model) {

        System.out.println("/store/updateStore : GET");

        int storeId = storeService.getStoreId(userName);

        if (storeId == 0) {

            return null;
        }

        else {

        Store store = storeService.getStore(storeId);

        System.out.println(store);

        model.addAttribute("store", store);

        return "/test/store/updateStoreTest";
        }

    }

    @PostMapping("/updateStore")
    public String updateStore(@ModelAttribute Store store, Model model) {

        System.out.println("/store/updateStore : POST");

        System.out.println(store);

        storeService.updateStore(store);

        StoreOperation storeOperation = storeService.getOperation(store.getStoreId());

        System.out.println(storeOperation);

        model.addAttribute("storeOperation", storeOperation);
        model.addAttribute("userName", store.getUserName());
        model.addAttribute("storeId", store.getStoreId());

        return "/test/store/updateOperationTest";
    }


    // 가게 운영 정보 수정
    @PostMapping("/updateOperation")
    public String updateOperation(@ModelAttribute StoreOperation storeOperation, Model model) {

        System.out.println("/store/updateOperation : POST");

        System.out.println(storeOperation);

        storeService.updateOperation(storeOperation);

        Store store = storeService.getStore(storeOperation.getStoreId());

        model.addAttribute("store", store);

        return "/test/store/updateStoreTestResult";
    }


    // 매장 소식 목록 조회
    @GetMapping
    public String getStoreNewsList() {

        System.out.println("/store/getStoreNewsList : GET");

        return null;
    }
}
