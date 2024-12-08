package com.placeHere.server.service.reservation.impl;

import com.placeHere.server.dao.reservation.ReservationDao;
import com.placeHere.server.dao.store.StoreDao;
import com.placeHere.server.domain.*;
import com.placeHere.server.service.reservation.PaymentService;
import com.placeHere.server.service.reservation.ReservationService;
import com.placeHere.server.service.pointShop.PointService;
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

    @Autowired
    @Qualifier("pointServiceImpl")
    PointService pointService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    private StoreDao storeDao;


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
        Reservation reservation = reservationDao.getRsrv(rsrvNo);

        if("리뷰 작성".equals(rsrvStatus)){
            String username = reservation.getUserName();
            int tranPoint = 100;
            String depType = "리뷰 작성";
            int currPoint = pointService.getCurrentPoint(username);

            pointService.addPointTransaction(username, tranPoint, depType, currPoint, rsrvNo );

            pointService.updatePoint(username, tranPoint);
        }

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

    // 예약 확정이면서 예약 일시가 지나간 예약 번호 리스트
    public List<Integer> getPastRsrvNos() throws Exception {
        return reservationDao.getPastRsrvNos();
    }

    public void updateRsrvStatusDay() throws Exception {
        // 지나간 예약 확정 상태의 예약 번호 리스트 가져오기
        List<Integer> pastRsrvNos = reservationDao.getPastRsrvNos();

        if (pastRsrvNos != null && !pastRsrvNos.isEmpty()) {
            for (int rsrvNo : pastRsrvNos) {
                // 각 예약 번호에 대해 상태를 업데이트
                reservationDao.updateRsrvStatus(rsrvNo, "이용 완료");
                Reservation reservation = reservationDao.getRsrv(rsrvNo);

                String username = reservation.getUserName();
                int tranPoint = 600;
                String depType = "예약 이용 완료";
                int currPoint = pointService.getCurrentPoint(username);

                pointService.addPointTransaction(username, tranPoint, depType, currPoint, rsrvNo );

                pointService.updatePoint(username, tranPoint);
            }
            System.out.println("Updated reservations: " + pastRsrvNos);
        } else {
            System.out.println("No past confirmed reservations to update.");
        }
    }


    // 탈퇴 예정인 일반 회원의 예약 번호 리스트
    public List<Integer> getRemoveUserRsrvNos(String userName) throws Exception {
        return reservationDao.getRemoveUserRsrvNos(userName);
    }

    public StoreReservation getStoreReservation(Map<String, Object> params) throws Exception {
        int storeId = (Integer) params.get("storeId");
        String effectDt = (String) params.get("effectDt");
        java.sql.Date sqlEffectDt = java.sql.Date.valueOf(effectDt); // String -> java.sql.Date 변환
        StoreOperation operation = storeDao.getOperationByDt(storeId, sqlEffectDt);
        List<ReservationTimeStatus> reservationTimeStatusList = reservationDao.getRsrvTimeStatus(params);
        StoreReservation storeReservation = new StoreReservation();
        storeReservation.setStoreOperation(operation);
        storeReservation.setReservationTimeStatusList(reservationTimeStatusList);
        return storeReservation;
    }


    // 탈퇴 예정인 일반 회원의 일괄 환불
    public void getRemoveUserRefundPayment(String userName) throws Exception {
        // 지나간 예약 확정 상태의 예약 번호 리스트 가져오기
        List<Integer> removeUserRsrvNos = reservationDao.getRemoveUserRsrvNos(userName);

        if (removeUserRsrvNos != null && !removeUserRsrvNos.isEmpty()) {
            for (int rsrvNo : removeUserRsrvNos) {

                reservationDao.updateRsrvStatus(rsrvNo, "예약 취소");

                Reservation reservation = reservationDao.getRsrv(rsrvNo);

                System.out.println(reservation.getAmount());

                paymentService.refundPayment(reservation.getPaymentId(), "회원 탈퇴로 인한 환불" , reservation.getRsrvDt(), reservation.getAmount());
            }
            System.out.println("예약 번호에서 환불할 내역: " + removeUserRsrvNos);
        } else {
            System.out.println("예약 번호에서 환불할 내역이 없습니다.");
        }
    }


    // 탈퇴 예정인 점주 회원의 예약 번호 리스트
    public List<Integer> getRemoveStoreRsrvNos(int storeId) throws Exception {
        return reservationDao.getRemoveStoreRsrvNos(storeId);
    }

    // 탈퇴 예정인 점주 회원의 일괄 환불
    public void getRemoveStoreRefundPayment(int storeId) throws Exception {
        // 지나간 예약 확정 상태의 예약 번호 리스트 가져오기
        List<Integer> removeStoreRsrvNos = reservationDao.getRemoveStoreRsrvNos(storeId);

        if (removeStoreRsrvNos != null && !removeStoreRsrvNos.isEmpty()) {
            for (int rsrvNo : removeStoreRsrvNos) {

                reservationDao.updateRsrvStatus(rsrvNo, "예약 취소");

                Reservation reservation = reservationDao.getRsrv(rsrvNo);

                paymentService.refundPayment(reservation.getPaymentId(), "가게 탈퇴로 인한 환불", null, reservation.getAmount());
            }
            System.out.println("예약 번호에서 환불할 내역: " + removeStoreRsrvNos);
        } else {
            System.out.println("예약 번호에서 환불할 내역이 없습니다.");
        }
    }



}
