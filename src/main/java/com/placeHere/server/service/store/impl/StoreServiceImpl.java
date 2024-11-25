package com.placeHere.server.service.store.impl;

import com.placeHere.server.dao.store.StoreDao;
import com.placeHere.server.domain.*;
import com.placeHere.server.service.store.StoreService;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Service("storeServiceImpl")
public class StoreServiceImpl implements StoreService {

    // Field
    @Autowired
    @Qualifier("storeDao")
    private StoreDao storeDao;

    public StoreServiceImpl() {
        super();
    }


    // Method
    // 사업자번호 중복확인 (DB) - Rest 에서 사용
    @Override
    public int chkDuplicateBusinessNo(String businessNo) {

        return storeDao.chkDuplicateBusinessNo(businessNo);
    }


    // 가게 등록 (return 되는 값은 store_id 의 값) TEST
    @Override
    public void addStore(Store store) {

        System.out.println("\nStoreDao.addStore()");

        // 매장 사진이 5개가 안되면 남는 부분 null 로 넣기
        List<String> storeImgList = store.getStoreImgList();

        if (storeImgList.size() < 5) {

            while (storeImgList.size() < 5) {
                storeImgList.add(null);

            }

        }

        System.out.println("storeImgList= "+storeImgList);
        store.setStoreImgList(storeImgList);

        // INSERT 되는 TABLE : store, amenities, menu
        storeDao.addStore(store);
        int storeId = storeDao.getStoreId(store.getBusinessNo());
        System.out.println("\tstoreId= "+storeId);
        storeDao.addMenu(storeId, store.getMenuList());

        if (store.getAmenitiesNoList() !=null && !store.getAmenitiesNoList().isEmpty()) {
            storeDao.addAmenities(storeId, store.getAmenitiesNoList());
        }

    }


    // 가게 정보 조회 (특정 날짜)
    @Override
    public Store getStore(int storeId, Date effectDt) {

        Store store = storeDao.getStore(storeId);
        StoreOperation storeOperation = storeDao.getOperationByDt(storeId, effectDt);
        store.setStoreOperation(storeOperation);

        List<String> storeImgList = new ArrayList<String>();
        storeImgList.add(store.getStoreImg1());
        storeImgList.add(store.getStoreImg2());
        storeImgList.add(store.getStoreImg3());
        storeImgList.add(store.getStoreImg4());
        storeImgList.add(store.getStoreImg5());
        store.setStoreImgList(storeImgList);

        return store;
    }


    // 가게 정보 조회 (최신)
    @Override
    public Store getStore(int storeId) {

        Store store = storeDao.getStore(storeId);
        StoreOperation storeOperation = storeDao.getCurrOperation(storeId);
        store.setStoreOperation(storeOperation);

        List<String> storeImgList = new ArrayList<String>();
        storeImgList.add(store.getStoreImg1());
        storeImgList.add(store.getStoreImg2());
        storeImgList.add(store.getStoreImg3());
        storeImgList.add(store.getStoreImg4());
        storeImgList.add(store.getStoreImg5());
        store.setStoreImgList(storeImgList);

        return store;
    }


    // 가게 검색, 가게 목록 조회
    @Override
    public List<Store> getStoreList(Search search) {

        return storeDao.getStoreList(search);
    }


    // 가게 수정 TEST
    @Override
    public void updateStore(Store store) {

        System.out.println("\nStoreDao.updateStore()");

        // 매장 사진이 5개가 안되면 남는 부분 null 로 넣기
        List<String> storeImgList = store.getStoreImgList();

        if (storeImgList.size() < 5) {

            while (storeImgList.size() < 5) {
                storeImgList.add(null);

            }

        }

        // UPDATE 되는 TABLE : store, amenities, menu
        storeDao.updateStore(store);
        storeDao.removeAmenities(store.getStoreId());
        storeDao.removeMenu(store.getStoreId());
        storeDao.addMenu(store.getStoreId(), store.getMenuList());

        if (store.getAmenitiesNoList() !=null && !store.getAmenitiesNoList().isEmpty()) {
            storeDao.addAmenities(store.getStoreId(), store.getAmenitiesNoList());
        }

    }


    // 가게 삭제 (DELETE 아니고 storeStatus 를  0에서 1로 변경) TEST
    @Override
    public void removeStore(int storeId) {

        System.out.println("\nStoreDao.removeStore()");

        int result = 0;

        storeDao.removeStore(storeId);
        System.out.println("\tstoreRemoveResult= "+result);

    }


    // 가게 운영 등록 TEST
    @Override
    public void addOperation(StoreOperation storeOperation) {

        storeOperation.setEffectDt(Date.valueOf(LocalDate.now()));
        storeDao.addOperation(storeOperation);

    }

    // 가게 운영 수정 (현재날짜+14일후 적용) TEST
    @Override
    public void updateOperation(StoreOperation storeOperation) {

        storeOperation.setEffectDt(Date.valueOf(LocalDate.now().plusDays(14)));
        storeDao.addOperation(storeOperation);

    }


    // 가게 운영 조회 (가게의 해당 날짜에 적용되고 있는 가게 운영 정보를 조회한다. 예약할때 사용 - Rest)
    @Override
    public StoreOperation getOperation(int storeId, Date effectDt) {

        StoreOperation storeOperation = storeDao.getOperationByDt(storeId, effectDt);

        List<String> regularClosedayList = new ArrayList<>();
        regularClosedayList.add(storeOperation.getRegularCloseday1());
        regularClosedayList.add(storeOperation.getRegularCloseday2());
        regularClosedayList.add(storeOperation.getRegularCloseday3());
        storeOperation.setRegularClosedayList(regularClosedayList);

        storeOperation.setClosedayList(storeDao.getClosedayList(storeId));

        return storeOperation;
    }


    // 최신 가게 운영 조회 (해당 가게의 최신 운영 조회. updateStore 에서 사용)
    @Override
    public StoreOperation getOperation(int storeId) {

        StoreOperation storeOperation = storeDao.getCurrOperation(storeId);

        List<String> regularClosedayList = new ArrayList<>();
        regularClosedayList.add(storeOperation.getRegularCloseday1());
        regularClosedayList.add(storeOperation.getRegularCloseday2());
        regularClosedayList.add(storeOperation.getRegularCloseday3());
        storeOperation.setRegularClosedayList(regularClosedayList);

        storeOperation.setClosedayList(storeDao.getClosedayList(storeId));

        return storeOperation;
    }


    // 매장 소식 등록 TEST
    @Override
    public void addStoreNews(StoreNews storeNews) {

        storeDao.addStoreNews(storeNews);

    }


    // 매장 소식 목록 조회 TEST
    @Override
    public List<StoreNews> getStoreNewsList(int storeId) {

        List<StoreNews> storeNewsList = storeDao.getStoreNewsList(storeId);

        return storeNewsList;
    }


    // 매장 소식 수정 TEST
    @Override
    public void updateStoreNews(StoreNews storeNews) {

        storeDao.updateStoreNews(storeNews);

    }


    // 매장 소식 삭제 (DELETE) TEST
    @Override
    public void removeStoreNews(int newsId) {

        storeDao.removeStoreNews(newsId);

    }


    // 휴무일 등록 TEST
    @Override
    public void addCloseday(int storeId, Date closeday) {

        storeDao.addCloseday(storeId, closeday);

    }


    // 휴무일 목록 조회 TEST
    @Override
    public List<Date> getClosedayList(int storeId, Search search) {

        List<Date> closedayList = storeDao.getClosedayListBySearch(storeId, search);

        return closedayList;
    }


    // 휴무일 삭제 (DELETE) TEST
    @Override
    public void removeCloseday(int closedayId) {

        storeDao.removeCloseday(closedayId);

    }


    // 가게 주변 시설 추천 (구글 API) TODO
    @Override
    public Map<String, Place> getNearbyPlaces(String storeAddr) {

        return Map.of();
    }


    // 가게 예약 통계 (RsrvDao 사용) TODO
    @Override
    public Map<String, Map<String, Integer>> getStatistics(int storeId) {

        return Map.of();
    }
}
