package com.placeHere.server.dao.store;

import com.placeHere.server.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Date;
import java.util.List;
//import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreDao {

    // Method
    // 사업자번호 중복확인 (DB) - Rest 에서 사용
    public int chkDuplicateBusinessNo(String businessNo);

    // 가게 등록 (return 되는 값은 store_id 의 값) TEST
    public int addStore(Store store);

    // 가게 정보 조회
    public Store getStore(int storeId);

    // 가게 검색, 가게 목록 조회
    public List<Store> getStoreList(Search search);

    // 가게 수정 TEST
    public int updateStore(Store store);

    // 가게 삭제 (DELETE 아니고 storeStatus 를  0에서 1로 변경) TEST
    public int removeStore(int storeId);

    // 편의시설 등록 (편의시설이 없으면 등록X) TEST
    public int addAmenities(int storeId, List<Integer> amenitiesNoList);

    // 편의시설 삭제 (통채로 삭제후 다시 등록) TEST
    public int removeAmenities(int storeId);

    // 메뉴 등록 TEST
    public int addMenu(int storeId, List<Menu> menuList);

    // 메뉴 삭제 (통채로 삭제후 다시 등록) TEST
    public int removeMenu(int storeId);

    // 가게 운영 등록 TODO
    public int addOperation(StoreOperation storeOperation);

    // 가게 운영 조회 (가게의 해당 날짜에 적용되고 있는 가게 운영 정보를 조회한다. 예약할때 사용 - Rest)
    public StoreOperation getOperationByDt(int storeId, Date effectDt);

    // 최신 가게 운영 조회 (해당 가게의 최신 운영 조회. updateStore 에서 사용)
    public StoreOperation getCurrOperation(int storeId);

    // 매장 소식 등록 TODO
    public int addStoreNews(StoreNews storeNews);

    // 매장 소식 목록 조회 TODO
    public List<StoreNews> getStoreNewsList(int storeId);

    // 매장 소식 수정 TODO
    public int updateStoreNews(StoreNews storeNews);

    // 매장 소식 삭제 (DELETE) TODO
    public int removeStoreNews(int newsId);

    // 휴무일 등록 TODO
    public int addCloseday(int storeId, Date closeday);

    // 휴무일 목록 조회 TODO
    public List<Date> getClosedayList(int storeId, Search search);

    // 휴무일 삭제 (DELETE) TODO
    public int removeCloseday(int closedayId);

}
