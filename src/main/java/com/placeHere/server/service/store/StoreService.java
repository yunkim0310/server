package com.placeHere.server.service.store;

import com.placeHere.server.domain.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface StoreService {

    // Method
    // 사업자번호 중복확인 (DB) - Rest 에서 사용
    public int chkDuplicateBusinessNo(String businessNo);

    // 가게 Id 조회
    public int getStoreId(String userName);

    // 가게 등록 (return 되는 값은 store_id 의 값)
    public int addStore(Store store);

    // 가게 정보 조회 (특정 날짜)
    public Store getStore(int storeId, Date effectDt);

    // 가게 정보 조회 (최신)
    public Store getStore(int storeId);

    // 가게 검색, 가게 목록 조회
    public List<Store> getStoreList(Search search);

    // 가게 목록 조회 (인기 가게)
    public List<Store> getStoreList(List<Integer> storeIdList);

    // 가게 목록 위치 조회
    public List<Map<String, String>> getStoreLocationList(Search search);

    // 가게 위치 조회
    public List<Map<String, String>> getStoreLocation(int storeId);

    // 가게 수정
    public void updateStore(Store store, boolean amenitiesEquals, boolean menuEquals);

    // 가게 삭제 (DELETE 아니고 storeStatus 를  0에서 1로 변경)
    public void removeStore(int storeId);

    // 가게 운영 등록
    public void addOperation(StoreOperation storeOperation);

    // 가게 운영 수정 (현재날짜+14일후 적용)
    public void updateOperation(StoreOperation storeOperation);

    // 가게 운영 조회 (가게의 해당 날짜에 적용되고 있는 가게 운영 정보를 조회한다. 예약할때 사용 - Rest)
    public StoreOperation getOperation(int storeId, Date effectDt);

    // 최신 가게 운영 조회 (해당 가게의 최신 운영 조회. updateStore 에서 사용)
    public StoreOperation getOperation(int storeId);

    // 매장 소식 등록
    public void addStoreNews(StoreNews storeNews);

    // 매장 소식 목록 조회
    public List<StoreNews> getStoreNewsList(int storeId, Search search);

    // 매장 소식 수정
    public void updateStoreNews(StoreNews storeNews);

    // 매장 소식 삭제 (DELETE)
    public void removeStoreNews(int newsId);

    // 휴무일 등록
    public void addCloseday(Closeday closeday);

    // 휴무일 목록 조회
    public List<Closeday> getClosedayList(int storeId, Search search);

    // 휴무일 삭제 (DELETE)
    public boolean removeCloseday(int closedayId);

    // 가게 예약 통계 (RsrvDao 사용)
    public Map<String, Map<String, Integer>> getStatistics(int storeId);
}
