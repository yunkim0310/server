package com.placeHere.server.service.reservation;

import com.placeHere.server.domain.Reservation;

import java.util.Date;

public interface ReservationService {

    // 예약 정보 등록
    public void addRsrv(Reservation reservation) throws Exception;

    // 예약 정보 조회
    public Reservation getRsrv(int rsrvNo) throws Exception;

    // 예약 상태 업데이트
    public void updateRsrvStatus(int rsrvNo, String rsrvStatus) throws Exception;

    // 예약 결제 고유 ID 업데이트
    public void updateRsrvpay(int rsrvNo, String paymentId) throws Exception;

    // 예약 환불 사유 업데이트
    public void updateRsrvReason(int rsrvNo, String reason) throws Exception;

    // 예약 목록 조회 미완
    public void getRsrvList(int rsrvNo, String reason) throws Exception;

    // 예약 일시(rsrsDate)의 예약 인수들의 합 미완
    public int countRsrv(Date rsrvDt) throws Exception;

    // 예약 날짜의 예약 인수들의 합 미완
    public void countDayRsrv(int rsrvNo, String reason) throws Exception;

    // 예약 검색(예약 상태, 예약 날짜) 미완
    public void searchRsrv(int rsrvNo, String reason) throws Exception;

    // 탈퇴할 회원의 예약 권수 카운팅 미완
    public void chkRsrv(int rsrvNo, String reason) throws Exception;

    // 한솔 예정
    public void calcRsrvPercent(int rsrvNo, String paymentId) throws Exception;

    // 한솔 예정
    public void cntWeekRsrv(int rsrvNo, String paymentId) throws Exception;

    // 한솔 예정
    public void cntRsrvAvg(int rsrvNo, String paymentId) throws Exception;


}