package com.placeHere.server.service.reservation.impl;

import com.placeHere.server.dao.reservation.ReservationDao;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.service.reservation.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

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


    // 예약 목록 조회 미완
    public void getRsrvList(int rsrvNo, String reason) throws Exception {
        reservationDao.getRsrvList(rsrvNo, reason);
    }


    // 예약 일시(rsrsDate)의 예약 인수들의 합 미완
    public int countRsrv(Date rsrvDt) throws Exception {
        return reservationDao.countRsrv(rsrvDt);
    }


    // 예약 날짜의 예약 인수들의 합 미완
    public void countDayRsrv(int rsrvNo, String reason) throws Exception {
        reservationDao.countDayRsrv(rsrvNo, reason);
    }


    // 예약 검색(예약 상태, 예약 날짜) 미완
    public void searchRsrv(int rsrvNo, String reason) throws Exception {
        reservationDao.searchRsrv(rsrvNo, reason);
    }


    // 탈퇴할 회원의 예약 권수 카운팅 미완
    public void chkRsrv(int rsrvNo, String reason) throws Exception {
        reservationDao.chkRsrv(rsrvNo, reason);
    }


    // 한솔 예정
    public void calcRsrvPercent(int rsrvNo, String paymentId) throws Exception {
        reservationDao.calcRsrvPercent(rsrvNo, paymentId);
    }


    // 한솔 예정
    public void cntWeekRsrv(int rsrvNo, String paymentId) throws Exception {
        reservationDao.cntWeekRsrv(rsrvNo, paymentId);
    }


    // 한솔 예정
    public void cntRsrvAvg(int rsrvNo, String paymentId) throws Exception {
        reservationDao.cntRsrvAvg(rsrvNo, paymentId);
    }

}
