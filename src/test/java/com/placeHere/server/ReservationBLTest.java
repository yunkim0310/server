package com.placeHere.server;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.service.reservation.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ReservationBLTest {

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService reservationService;

    @Test
    public void test() {
        System.out.println("1234");
    }


    @Test
    public void addRsrv() throws Exception{
        Reservation reservation = new Reservation();
        reservation.setStoreId(1);
        reservation.setUserName("user3");
        reservation.setRsrvStatus("전화 예약");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date customDate = sdf.parse("2024-11-29 21:30"); // 임의의 날짜 설정
        reservation.setRsrvDt(customDate);

        reservation.setRsrvPerson(2);
        reservation.setAmount(10000);
        reservation.setRsrvReq(null);
        reservation.setStoreName("보배 반점");
        reservation.setStoreAddr("서울 강남구");
        reservation.setRsrvNumber("01056484822");

        // When: 예약 추가 메서드 실행
        reservationService.addRsrv(reservation);
    }


    @Test
    public void addRsrvStore() throws Exception{
        Reservation reservation = new Reservation();
        reservation.setStoreId(1);
        reservation.setUserName("store1");
        reservation.setRsrvStatus("전화 예약");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date customDate = sdf.parse("2024-11-29 21:30"); // 임의의 날짜 설정
        reservation.setRsrvDt(customDate);

        reservation.setRsrvPerson(2);
        reservation.setRsrvReq(null);
        reservation.setStoreName("보배 반점");
        reservation.setStoreAddr("서울 강남구");
        reservation.setRsrvNumber("01056484822");

        // When: 예약 추가 메서드 실행
        reservationService.addRsrvStore(reservation);
    }


    @Test
    public void updateRsrvStatus() throws Exception {
        // Given: 테스트할 예약 번호와 변경할 상태 값
        int rsrvNo = 4; // 테스트용 예약 번호
        String rsrvStatus = "전화 예약"; // 상태 업데이트 값

        // When: 예약 상태를 업데이트
        reservationService.updateRsrvStatus(rsrvNo, rsrvStatus);

        // Then: 상태가 업데이트되었는지 확인
        // 이 부분은 실제로 DB에서 조회하여 확인하는 코드가 필요
        System.out.println("예약 번호: " + rsrvNo + "의 상태가 " + rsrvStatus + "로 업데이트되었습니다.");
    }


    @Test
    public void updateRsrvpay() throws Exception {
        // Given: 테스트할 예약 번호와 변경할 상태 값
        int rsrvNo = 6; // 테스트용 예약 번호
        String paymentId = "sdffqgqgaff"; // 결제 고유 아이디 업데이트 값

        // When: 결제 고유 아이디를 업데이트
        reservationService.updateRsrvpay(rsrvNo, paymentId);

        // Then: 결제 고유 ID가 업데이트되었는지 확인
        // 이 부분은 실제로 DB에서 조회하여 확인하는 코드가 필요
        System.out.println("예약 번호: " + rsrvNo + "의 결제 고유 ID가 " + paymentId + "로 업데이트되었습니다.");
    }


    @Test
    public void updateRsrvReason() throws Exception {
        // Given: 테스트할 예약 번호와 변경할 상태 값
        int rsrvNo = 2; // 테스트용 예약 번호
        String reason = "시원하게 에어컨 온도 좀 낮쳐주세요"; // 예약 요청 사항 업데이트 값

        // When: 예약 요청 사항을 업데이트
        reservationService.updateRsrvReason(rsrvNo, reason);

        // Then: 예약 요청 사항이 업데이트되었는지 확인
        // 이 부분은 실제로 DB에서 조회하여 확인하는 코드가 필요
        System.out.println("예약 번호: " + rsrvNo + "의 예약 요청 사항이 " + reason + "로 업데이트되었습니다.");
    }


    @Test
    public void getRsrv() throws Exception {
        // Given: 테스트할 예약 번호
        int rsrvNo = 2; // 테스트용 예약 번호 (사전에 존재해야 함)

        // When: 예약 정보를 가져오는 메서드 호출
        Reservation reservation = reservationService.getRsrv(rsrvNo);

        // Then: 가져온 데이터 검증
        System.out.println("조회된 예약 정보:");
        System.out.println("예약 번호: " + reservation.getRsrvNo());
        System.out.println("상점 ID: " + reservation.getStoreId());
        System.out.println("예약 상태: " + reservation.getRsrvStatus());
        System.out.println("예약 일시: " + reservation.getRsrvDt());
        System.out.println("예약 인원: " + reservation.getRsrvPerson());
        System.out.println("결제 금액: " + reservation.getAmount());
        System.out.println("결제 ID: " + reservation.getPaymentId());
        System.out.println("요청 사항: " + reservation.getRsrvReq());
        System.out.println("사유: " + reservation.getReason());
        System.out.println("상점 이름: " + reservation.getStoreName());
        System.out.println("상점 주소: " + reservation.getStoreAddr());
    }


    @Test
    public void testGetCountRsrv() throws Exception {
        // Given: 테스트 데이터
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date targetDate = sdf.parse("2024-11-29 21:30:00");
        int storeId = 1;

        // When: 서비스 메서드 호출
        reservationService.getCountRsrv(targetDate, storeId);

        // Then: 결과 출력
        System.out.println("2024-11-29 21:30:00 가게 번호 " + storeId + "의 총 예약 인원: " + reservationService.getCountRsrv(targetDate, storeId));
    }


    @Test
    public void testGetCountDayRsrv() throws Exception {

        // Assert로 기대 값 검증 (예: 예상 값이 10명이라고 가정)
        // Assertions.assertEquals(10, totalPersons);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date targetDate = sdf.parse("2024-11-29");
        int storeId = 1;

        // When: 서비스 메서드 호출
        reservationService.getCountDayRsrv(targetDate, storeId);

        // Then: 결과 출력
        System.out.println("2024-11-29 가게 번호 " + storeId + "의 총 예약 인원: " + reservationService.getCountDayRsrv(targetDate, storeId));
    }


    @Test
    public void testGetCountRsrvUser() throws Exception {
        // Given: 테스트 데이터

        String userName = "user3";

        // When: 서비스 메서드 호출
        reservationService.getCountRsrvUser(userName);

        // Then: 결과 출력
        System.out.println("일반회원 " + userName + "의 미완료 예약 권수: " + reservationService.getCountRsrvUser(userName));
    }


    @Test
    public void testGetCountRsrvStore() throws Exception {
        // Given: 테스트 데이터

        int storeId = 1;

        // When: 서비스 메서드 호출
        reservationService.getCountRsrvStore(storeId);

        // Then: 결과 출력
        System.out.println("점주회원 " + storeId + "의 미완료 예약 권수: " + reservationService.getCountRsrvStore(storeId));
    }


    @Test
    public void testGetCountRsrvNumber() throws Exception {
        // Given: 테스트 데이터

        int storeId = 1;

        // When: 서비스 메서드 호출
        reservationService.getCountRsrvNumber(storeId);

        // Then: 결과 출력
        System.out.println("점주회원 " + storeId + "의 미완료 전화 예약 권수: " + reservationService.getCountRsrvNumber(storeId));
    }


    @Test
    public void testGetRsrvList() throws Exception {
        // When: 어드민용 서비스 호출
        List<Map<String, Object>> reservations = reservationService.getRsrvList();

        // Then: 결과 출력
        for (Map<String, Object> reservation : reservations) {
            System.out.println("Admin Reservation: " + reservation);
        }
    }


    @Test
    public void testGetRsrvUserList() throws Exception {

        String userName = "user3";
        // When: 어드민용 서비스 호출
        List<Map<String, Object>> reservations = reservationService.getRsrvUserList(userName);

        // Then: 결과 출력
        for (Map<String, Object> reservation : reservations) {
            System.out.println("User Reservation: " + reservation);
        }
    }


    @Test
    public void testGetRsrvStoreList() throws Exception {

        int storeId = 1;
        // When: 어드민용 서비스 호출
        List<Map<String, Object>> reservations = reservationService.getRsrvStoreList(storeId);

        // Then: 결과 출력
        for (Map<String, Object> reservation : reservations) {
            System.out.println("Store Reservation: " + reservation);
        }
    }



}
