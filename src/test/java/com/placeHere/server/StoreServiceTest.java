package com.placeHere.server;

import com.placeHere.server.domain.Store;
import com.placeHere.server.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

    @Test
    public void testAddStore() {

//        Store store = new Store();
//        store.setUserName("store2");
//        store.setBusinessNo("2160571255");
//        store.setStoreName("트로이 케밥");
//        store.setStoreAddr("서울 강남구 테헤란로1길 16, 1층");
//        store.setStorePhone("0224593929");
//        store.setStoreInfo("현지인이 운영하는 케밥집!");
//        store.setFoodCategoryId("040199/케밥");
//        store.setSpecialMenuNo(2);
//        List<String> storeImgList = new ArrayList<>(List.of("storeImg1", "storeImg2", "storeImg3"));
//        store.setStoreImgList(storeImgList);
//        List<String> hashtagList = new ArrayList<String>();
//        hashtagList.add("케밥");
//        hashtagList.add("강남");
//        hashtagList.add("터키음식");
//        store.setHashtagList(hashtagList);
//
//        storeService.addStore(store);

        List<String> storeImgList = new ArrayList<>(List.of("storeImg1", "storeImg2", "storeImg3"));

        System.out.println(storeImgList);
        System.out.println(storeImgList.size());
        System.out.println(5-storeImgList.size());

        if (storeImgList.size() < 5) {

            for (int i = 0; i < 5 - storeImgList.size(); i++) {
//                storeImgList.add(storeImgList.size()+i,null);
                System.out.println("index= "+i);
                System.out.println(storeImgList);
            }

        }


/*
("store2", "2160571255", "트로이 케밥", "서울 강남구 테헤란로1길 16, 1층", "0224593929",
	 "현지인이 운영하는 케밥집!", "040199/케밥", 2,
	 "storeImg1", "storeImg2", "storeImg3",
	 NULL, NULL,
	 "케밥,강남,터키음식")

* */

    }

//    @Test
//    public void testRemoveStore() {
//
//        storeService.removeStore(1);
//    }

//    @Test
//    public void testChkDuplicateBusinessNo() {
//        int result = storeService.chkDuplicateBusinessNo("6575100638");
//
//        System.out.println(result);
//    }

}
