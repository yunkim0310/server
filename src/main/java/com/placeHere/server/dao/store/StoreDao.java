package com.placeHere.server.dao.store;

import com.placeHere.server.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
//import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreDao {

    // Method
    // 사업자번호 중복확인 (DB) - Rest 에서 사용
    public int chkDuplicateBusinessNo(String businessNo);

    // 가게 등록 (return 되는 값은 store_id 의 값)
    public int addStore(Store store);

    // 가게 Id 조회
    public int getStoreId(String userName);

    // 가게 정보 조회
    public Store getStore(int storeId);

    // 가게 검색, 가게 목록 조회
    public List<Store> getStoreList(Search search);

    // 가게 수정
    public void updateStore(Store store);

    // 가게 삭제 (DELETE 아니고 storeStatus 를  0에서 1로 변경)
    public void removeStore(int storeId);

    // 편의시설 등록 (편의시설이 없으면 등록X)
    public void addAmenities(@Param("storeId") int storeId, @Param("amenitiesNoList") List<Integer> amenitiesNoList);

    // 편의시설 목록 조회
    public List<Integer> getAmenitiesList(int storeId);

    // 편의시설 삭제 (통채로 삭제후 다시 등록)
    public void removeAmenities(int storeId);

    // 메뉴 등록
    public void addMenu(@Param("storeId") int storeId, @Param("menuList") List<Menu> menuList);

    // 메뉴 목록 조회
    public List<Menu> getMenuList(int storeId);

    // 메뉴 삭제 (통채로 삭제후 다시 등록)
    public void removeMenu(int storeId);

    // 가게 운영 등록 TEST
    public void addOperation(StoreOperation storeOperation);

    // 가게 운영 조회 (가게의 해당 날짜에 적용되고 있는 가게 운영 정보를 조회한다. 예약할때 사용 - Rest)
    public StoreOperation getOperationByDt(@Param("storeId") int storeId, @Param("effectDt") Date effectDt);

    // 최신 가게 운영 조회 (해당 가게의 최신 운영 조회. updateStore 에서 사용)
    public StoreOperation getCurrOperation(int storeId);

    // 매장 소식 등록 TEST
    public void addStoreNews(StoreNews storeNews);

    // 매장 소식 목록 조회 TEST
    public List<StoreNews> getStoreNewsList(@Param("storeId") int storeId, @Param("search") Search search);

    // 매장 소식 수정 TEST
    public void updateStoreNews(StoreNews storeNews);

    // 매장 소식 삭제 (DELETE) TEST
    public void removeStoreNews(int newsId);

    // 휴무일 등록 TEST
    public void addCloseday(@Param("storeId") int storeId, @Param("closeday") Date closeday);

    // 휴무일 목록 조회 TEST
    public List<Date> getClosedayListBySearch(@Param("storeId") int storeId, @Param("search") Search search);

    // 휴무일 목록 조회 (오늘~14일후)
    public List<Date> getClosedayList(int storeId);

    // 휴무일 삭제 (DELETE) TEST
    public void removeCloseday(int closedayId);

    // 금주 요일별 예약횟수
    public List<Integer> cntWeekRsrv(int storeId);

    // 요일별 평균 예약횟수
    public List<Integer> cntRsrvAvg(int storeId);

    // 성별, 나이대별 예약비율
    public List<Map<String, Integer>> calcRsrvPercent(int storeId);

}
