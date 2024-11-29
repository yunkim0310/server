package com.placeHere.server.service.reservation.impl;

import com.placeHere.server.dao.reservation.ReservationDao;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("reservationServiceImpl")
public class ReservationServiceImpl implements ReservationService{

    @Autowired
    @Qualifier("reservationDao")
    ReservationDao reservationDao;


    // 예약 정보 등록
    public void addRsrv(Reservation reservation) throws Exception {
        reservation.setRsrvStatus("결제 중");
        reservationDao.addRsrv(reservation);
    }


    // 예약 정보 등록 점주
    public void addRsrvStore(Reservation reservation) throws Exception {
        reservation.setRsrvStatus("전화 예약");
        reservationDao.addRsrvStore(reservation);
    }


    // 예약 정보 조회
    public Reservation getRsrv(int rsrvNo) throws Exception {
        return reservationDao.getRsrv(rsrvNo);
    }


    // 예약 상태 업데이트
    public void updateRsrvStatus(int rsrvNo, String rsrvStatus) throws Exception {
        reservationDao.updateRsrvStatus(rsrvNo, rsrvStatus);
    }


    // 예약 결제 고유 ID 업데이트
    public void updateRsrvpay(int rsrvNo, String paymentId) throws Exception {
        reservationDao.updateRsrvpay(rsrvNo, paymentId);
    }


    // 예약 환불 사유 업데이트
    public void updateRsrvReason(int rsrvNo, String reason) throws Exception {
        reservationDao.updateRsrvReason(rsrvNo, reason);
    }

    // 예약 목록 조회 어드민
    public List<Reservation> getRsrvList() throws Exception{
        return reservationDao.getRsrvList();
    }


    // 예약 목록 조회 일반 회원
    public List<Reservation> getRsrvUserList(String userName, Search search) throws Exception {
        return reservationDao.getRsrvUserList(userName, search);
    }


    // 예약 목록 조회 점주
    public List<Reservation> getRsrvStoreList(int storeId, Search search) throws Exception {
        return reservationDao.getRsrvStoreList(storeId, search);
    }


    // 예약 일시(rsrsDate)의 예약 인수들의 합
    public int getCountRsrv(Date rsrvDt, int storeId) throws Exception {
        // Map으로 파라미터 생성
        Map<String, Object> params = new HashMap<>();
        params.put("rsrvDt", rsrvDt);
        params.put("storeId", storeId);

        // DAO 호출하여 결과 반환
        return reservationDao.getCountRsrv(params);
    }


    // 예약 날짜의 예약 인수들의 합(휴무일에도 쓰임)
    public int getCountDayRsrv(Date rsrvDt, int storeId) throws Exception {
        // Map으로 파라미터 생성
        Map<String, Object> params = new HashMap<>();
        params.put("rsrvDt", rsrvDt);
        params.put("storeId", storeId);

        // DAO 호출하여 결과 반환
        return reservationDao.getCountDayRsrv(params);
    }


    // 탈퇴할 회원의 예약 권수 카운팅
    public int getCountRsrvUser(String userName) throws Exception {
        return reservationDao.getCountRsrvUser(userName);
    }


    // 탈퇴할 점주 회원의 예약 권수 카운팅
    public int getCountRsrvStore(int storeId) throws Exception {
        return reservationDao.getCountRsrvStore(storeId);
    }


    // 탈퇴할 점주 회원의 전화 예약 권수 카운팅
    public int getCountRsrvNumber(int storeId) throws Exception {
        return reservationDao.getCountRsrvNumber(storeId);
    }



}
