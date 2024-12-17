package com.placeHere.server;

import com.placeHere.server.dao.store.StoreDao;
import com.placeHere.server.domain.*;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.store.SearchService;
import com.placeHere.server.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class StoreServiceTest {

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
    private SearchService searchService;

    @Autowired
    private StoreDao storeDao;

    @Value("${list_size}")
    private int listSize;

    @Value("${page_size}")
    private int pageSize;


    @Test
    public void addSearch() {

        Search search = new Search();
//        search.setSearchKeyword("강남 자장면 맛집 크리스마스");
        search.setSearchKeyword("강남");

        searchService.addSearch(search.getSearchKeyword());

    }


    @Test
    public void chkLike() throws Exception {

        Like like = new Like("user01");
        like.setTarget("store");
        like.setRelationNo(6);

        Like result = likeService.chkLike(like);

        System.out.println(result);

    }

    @Test
    public void getReviewList() throws Exception {

        Search search = new Search(pageSize, listSize);
        System.out.println(search);
//        List<Review> reviewList = communityService.getReviewList();

        List<Review> reviewList = communityService.getReviewList(1, search);

//        List<Review> reviewList = communityService.getReviewList(new ArrayList<>(List.of("user01","user02")));

        System.out.println(reviewList.size());
        System.out.println(reviewList);

    }

    @Test
    public void getlikeList() throws Exception {

//        List<Integer> likeList = likeService.likeList("store");
//
//        System.out.println(likeList.size());
//        System.out.println(likeList);

        Search search = new Search(pageSize,listSize);

        List<Like> storeLikeList = likeService.getStoreLikeList("user01", search);

        System.out.println(storeLikeList.size());
        System.out.println(storeLikeList);

    }


    @Test
    public void removeStoreNews() {

        storeService.removeStoreNews(13);

        StoreNews storeNews = new StoreNews();
        storeNews.setStoreId(1);
        storeNews.setNewsContents("보배반점 인스타 행사중~");

        Search search = new Search(pageSize, listSize);

//        storeService.addStoreNews(storeNews);

        List<StoreNews> storeNewsList = storeService.getStoreNewsList(1,search);
        System.out.println(storeNewsList.size());
        System.out.println(storeNewsList);

    }

    @Test
    public void addStoreNews() {

        StoreNews storeNews = new StoreNews();
        storeNews.setStoreId(1);
        storeNews.setNewsContents("보배반점 인스타 행사중~");

        Search search = new Search(pageSize, listSize);

        storeService.addStoreNews(storeNews);

        List<StoreNews> storeNewsList = storeService.getStoreNewsList(1,search);
        System.out.println(storeNewsList.size());
        System.out.println(storeNewsList);

    }


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

//        storeService.updateStore(store);

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

        Store store = storeService.getStore(15);
//        Store store = storeService.getStore(1, Date.valueOf("2024-11-26"));

        System.out.println(store);

    }

    @Test
    public void getStoreList() {

        Search search = new Search();
        search.setListSize(listSize);
        search.setSearchKeyword("감자탕");

//        List<String> regionList = new ArrayList<>(List.of("강남"));
//        search.setRegionList(regionList);

//        search.setFoodCategoryId("한식");

//        search.setAmenitiesNoList(List.of(1));

//        System.out.println(search);

        List<Store> storeList = storeService.getStoreList(search);
        System.out.println(storeList.size());
        System.out.println(storeList);
    }

    @Test
    public void getStatistics() {

        int storeId = 1;

//        System.out.println("금주 요일별 예약횟수");
//        List<Integer> cntWeekRsrvList = storeDao.cntWeekRsrv(storeId);
//        System.out.println(cntWeekRsrvList);
//
//        Map<String, Integer> cntWeekRsrvMap = new HashMap<String, Integer>();
//        List<String> weekName = new ArrayList<>(List.of("일", "월", "화", "수", "목", "금", "토"));
//        System.out.println(weekName);
//
//        for (int i = 0; i < weekName.size(); i++) {
//            cntWeekRsrvMap.put(weekName.get(i), cntWeekRsrvList.get(i));
//        }
//
//        System.out.println(cntWeekRsrvMap);
//
//        System.out.println("\n요일별 평균 예약횟수");
//        List<Integer> cntRsrvAvgList = storeDao.cntRsrvAvg(storeId);
//        System.out.println(cntRsrvAvgList);
//
//        Map<String, Integer> cntRsrvAvgMap = new HashMap<>();
//
//        for (int i = 0; i < weekName.size(); i++) {
//            cntRsrvAvgMap.put(weekName.get(i), cntRsrvAvgList.get(i));
//        }
//
//        System.out.println(cntRsrvAvgMap);
//
//        System.out.println("\n성별 나이대별 예약비율");
//        List<Map<String, Integer>> calcRsrvPercentList = storeDao.calcRsrvPercent(storeId);
//        System.out.println(calcRsrvPercentList);
//
//        Map<String, Integer> calcRsrvPercentMap = new HashMap<>();
//
//        Set<String> ageGenderList = new HashSet<>(Arrays.asList(
//                "10대 남성", "20대 남성", "30대 남성", "40대 남성", "50대 남성", "60대이상 남성",
//                "10대 여성", "20대 여성", "30대 여성", "40대 여성", "50대 여성", "60대이상 여성"
//        ));
//
//        for (Map<String, Integer> map : calcRsrvPercentList) {
//            calcRsrvPercentMap.put(map.values().toArray()[0].toString(), Integer.parseInt(map.values().toArray()[1].toString()));
//        }
//
//        System.out.println(calcRsrvPercentMap);
//
//        for (String ageGender : ageGenderList) {
//            calcRsrvPercentMap.putIfAbsent(ageGender, 0);
//        }
//
//        System.out.println(calcRsrvPercentMap);

        storeService.getStatistics(1);

    }

    @Test
    public void test() {

        StoreOperation storeOperation = new StoreOperation();
        storeOperation.setRegularClosedayList(new ArrayList<String>(List.of("월","금")));

        List<String> regularClosedayList = (storeOperation.getRegularClosedayList() == null) ? new ArrayList<>() : storeOperation.getRegularClosedayList();
        int regularClosedayCnt = regularClosedayList.size();

        if (regularClosedayCnt < 3) {

            for (int i = 0; i < 3 - regularClosedayCnt; i++) {
                regularClosedayList.add(null);
            }

        }

        System.out.println(regularClosedayList);
    }

    @Test
    public void updateOperation() {

        StoreOperation storeOperation = new StoreOperation();
        storeOperation.setStoreId(3);
        storeOperation.setOpenTime("09:30");
        storeOperation.setCloseTime("23:30");
        storeOperation.setRegularClosedayList(null);
        storeOperation.setSecurity(5000);
        storeOperation.setRsrvLimit(30);

        storeService.updateOperation(storeOperation);

    }

    @Test
    public void pagingTest() {

//        System.out.println(listSize);
        Search search = new Search(pageSize, listSize);

//        List<StoreNews> storeNewsList = storeService.getStoreNewsList(1, search);
//
//        System.out.println(storeNewsList.size());
//        System.out.println(storeNewsList);

//        System.out.println(search);

        List<Closeday> closedayList = storeService.getClosedayList(1, search);

        System.out.println(closedayList.size());
        System.out.println(closedayList);

//        System.out.println(storeDao.getClosedayList(1));

    }

    @Test
    public void getStoreId() {

        System.out.println(storeService.getStoreId("store08"));
    }


}
