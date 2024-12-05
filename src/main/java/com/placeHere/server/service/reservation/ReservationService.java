package com.placeHere.server.service.reservation;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Search;

import java.util.Date;
import java.util.List;


public interface ReservationService {

    // 예약 정보 등록
    public void addRsrv(Reservation reservation) throws Exception;

    // 예약 정보 등록 점주
    public void addRsrvStore(Reservation reservation) throws Exception;

    // 예약 정보 조회
    public Reservation getRsrv(int rsrvNo) throws Exception;

    // 예약 상태 업데이트
    public void updateRsrvStatus(int rsrvNo, String rsrvStatus) throws Exception;

    // 예약 결제 고유 ID 업데이트
    public void updateRsrvpay(int rsrvNo, String paymentId) throws Exception;


    // 예약 환불 사유 업데이트
    public void updateRsrvReason(int rsrvNo, String reason) throws Exception;

    // 예약 목록 조회 어드민
    public List<Reservation> getRsrvList() throws Exception;

    // 예약 목록 조회 일반 회원
    public List<Reservation> getRsrvUserList(String userName, Search search) throws Exception;

    // 예약 목록 조회 점주 회원
    public List<Reservation> getRsrvStoreList(int storeId, Search search) throws Exception;

    // 예약 일시(rsrsDate)의 예약 인수들의 합
    int getCountRsrv(Date rsrvDt, int storeId) throws Exception;

    // 예약 날짜의 예약 인수들의 합(휴무일에도 쓰임)
    public int getCountDayRsrv(Date rsrvDt, int storeId) throws Exception;

    // 탈퇴할 일반 회원의 예약 권수 카운팅
    public int getCountRsrvUser(String userName) throws Exception;

    // 탈퇴할 점주 회원의 예약 권수 카운팅 단, 전화 예약 제외 미완
    public int getCountRsrvStore(int storeId) throws Exception;

    // 탈퇴할 점주 회원의 전화 예약 권수 카운팅
    public int getCountRsrvNumber(int storeId) throws Exception;

    // 예약 확정이면서 예약 일시가 지나간 예약 번호 리스트
    List<Integer> getPastRsrvNos() throws Exception;

    // 예약 확정이면서 예약 일시가 지나간 예약 업데이트
    public void updateRsrvStatusDay() throws Exception;

    // 탈퇴 예정인 일반 회원의 예약 번호 리스트
    List<Integer> getRemoveUserRsrvNos(String userName) throws Exception;

    // 탈퇴 예정인 일반 회원의 일괄 환불
    public void getRemoveUserRefundPayment(String userName) throws Exception;

    // 탈퇴 예정인 점주 회원의 예약 번호 리스트
    List<Integer> getRemoveStoreRsrvNos(int storeId) throws Exception;

    // 탈퇴 예정인 점주 회원의 일괄 환불
    public void getRemoveStoreRefundPayment(int storeId) throws Exception;




}