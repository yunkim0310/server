package com.placeHere.server;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.service.reservation.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        reservation.setUserName("user04");
        reservation.setRsrvStatus("결제 중");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date customDate = sdf.parse("2024-11-22 10:30:00"); // 임의의 날짜 설정
        reservation.setRsrvDt(customDate);

        reservation.setRsrvPerson(2);
        reservation.setAmount(10000);
        reservation.setRsrvReq("");
        reservation.setStoreName("보배 반점");
        reservation.setStoreAddr("서울 강남구");
        reservation.setRsrvNumber("01056484822");

        // When: 예약 추가 메서드 실행
        reservationService.addRsrv(reservation);
    }

    @Test
    public void updateRsrvStatus() throws Exception {
        // Given: 테스트할 예약 번호와 변경할 상태 값
        int rsrvNo = 2; // 테스트용 예약 번호
        String rsrvStatus = "예약 확정"; // 상태 업데이트 값

        // When: 예약 상태를 업데이트
        reservationService.updateRsrvStatus(rsrvNo, rsrvStatus);

        // Then: 상태가 업데이트되었는지 확인
        // 이 부분은 실제로 DB에서 조회하여 확인하는 코드가 필요
        System.out.println("예약 번호: " + rsrvNo + "의 상태가 " + rsrvStatus + "로 업데이트되었습니다.");
    }


    @Test
    public void updateRsrvpay() throws Exception {
        // Given: 테스트할 예약 번호와 변경할 상태 값
        int rsrvNo = 2; // 테스트용 예약 번호
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

}
