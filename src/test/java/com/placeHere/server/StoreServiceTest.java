package com.placeHere.server;

import com.placeHere.server.dao.store.StoreDao;
import com.placeHere.server.domain.Menu;
import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.Store;
import com.placeHere.server.domain.StoreOperation;
import com.placeHere.server.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

    @Autowired
    private StoreDao storeDao;

//    @Test
    public void testAddStore() {

        Store store = new Store();
        store.setUserName("store2");
        store.setBusinessNo("2160571255");
        store.setStoreName("트로이 케밥");
        store.setStoreAddr("서울 강남구 테헤란로1길 16, 1층");
        store.setStorePhone("0224593929");
        store.setStoreInfo("현지인이 운영하는 케밥집!");
        store.setFoodCategoryId("040199/케밥");
        store.setSpecialMenuNo(2);

        List<String> storeImgList = new ArrayList<>(List.of("storeImg1", "storeImg2", "storeImg3"));
        store.setStoreImgList(storeImgList);

        List<String> hashtagList = new ArrayList<String>();
        hashtagList.add("케밥");
        hashtagList.add("강남");
        hashtagList.add("터키음식");
        store.setHashtagList(hashtagList);

        Menu menu1 = new Menu();
        menu1.setMenuNo(1);
        menu1.setMenuName("케밥(닭)");
        menu1.setMenuPrice(8500);
        
        Menu menu2 = new Menu(0, 0, 2, null, null, "케밥(양)", 9000, "터키 최고급 양고기 사용");
        
        List<Menu> menuList = new ArrayList<>(List.of(menu1, menu2));
        store.setMenuList(menuList);

        storeService.addStore(store);


    }

//    @Test
    public void updateStore() {

        Store store = new Store();
        store.setStoreId(13);
        store.setStoreName("트로이 케밥");
        store.setStoreAddr("서울 강남구 테헤란로1길 16, 1층");
        store.setStorePhone("0224593929");
        store.setStoreInfo("현지인이 운영하는 케밥집!!!!");
        store.setFoodCategoryId("040199/케밥");
        store.setSpecialMenuNo(1);

        List<String> storeImgList = new ArrayList<>(List.of("storeImg1", "storeImg2", "storeImg3", "storeImg4"));
        store.setStoreImgList(storeImgList);

        List<String> hashtagList = new ArrayList<String>();
        hashtagList.add("케밥");
        hashtagList.add("강남");
        hashtagList.add("터키음식");
        store.setHashtagList(hashtagList);

        Menu menu1 = new Menu();
        menu1.setMenuNo(1);
        menu1.setMenuName("케밥(닭)");
        menu1.setMenuPrice(9000);

        Menu menu2 = new Menu(0, 0, 2, null, null, "케밥(양)", 10000, "터키 최고급 양고기 사용!!!");

        List<Menu> menuList = new ArrayList<>(List.of(menu1, menu2));
        store.setMenuList(menuList);

        storeService.updateStore(store);

    }

//    @Test
    public void testRemoveStore() {

        storeService.removeStore(1);
    }

//    @Test
    public void addAmenities(){

        List<Integer> amenitiesNoList = new ArrayList<>(List.of(1,3,5));
        storeDao.addAmenities(12, amenitiesNoList);

    }

//    @Test
    public void removeAmenities(){

        storeDao.removeAmenities(12);

    }

//    @Test
    public void removeMenu(){

        storeDao.removeMenu(12);

    }

    @Test
    public void testChkDuplicateBusinessNo() {
        int result = storeService.chkDuplicateBusinessNo("6575100638");

        System.out.println(result);
    }

    @Test
    public void getOperationByDt() {

        LocalDate dayAfter = LocalDate.now().plusDays(3);
        Date a = Date.valueOf(dayAfter);
        Date day = Date.valueOf("2024-12-10");

        StoreOperation storeOperation =  storeService.getOperation(1, day);
        System.out.println(storeOperation);

    }

    @Test
    public void getCurrOperation() {

        StoreOperation storeOperation = storeService.getOperation(1);

        System.out.println(storeOperation);

    }

    @Test
    public void getMenuList() {
        List<Menu> menuList = storeDao.getMenuList(13);
        System.out.println(menuList);
    }

    @Test
    public void getAmenitiesList() {
        System.out.println(storeDao.getAmenitiesList(1));
    }

    @Test
    public void getStore() {

//        Store store = storeService.getStore(13);
        Store store = storeService.getStore(13, Date.valueOf("2024-12-10"));

        System.out.println(store);

    }

    @Test
    public void getStoreList() {

        Search search = new Search();
        search.setSearchKeyword("케밥");

//        List<String> regionList = new ArrayList<>(List.of("강남"));
//        search.setRegionList(regionList);

        search.setAmenitiesNoList(List.of(1));

//        System.out.println(search);

        List<Store> storeList = storeService.getStoreList(search);
        System.out.println(storeList.size());
        System.out.println(storeList);
    }

    @Test
    public void getStatistics() {

        int storeId = 2;

        List<Integer> cntWeekRsrv = storeDao.cntWeekRsrv(storeId);
        System.out.println(cntWeekRsrv);

        List<Integer> cntRsrvAvg = storeDao.cntRsrvAvg(storeId);
        System.out.println(cntRsrvAvg);

    }

}
