package com.placeHere.server.service.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final String REFUND_API_URL = "https://api.tosspayments.com/v1/payments/{paymentKey}/cancel";

    //환불 로직
    public String refundPayment(String paymentKey, String reason, Date rsrvDt, int amount) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic dGVzdF9za182YkpYbWdvMjhlTnliYnFKQWxtWTNMQW5HS1d4Og==");

        // 기본 값: 전액 환불
        int refundAmount = amount;

        if (rsrvDt != null) {
            // rsrvDt에서 날짜만 추출
            LocalDate reservationDate = rsrvDt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            // 날짜 차이 계산
            long daysUntilReservation = ChronoUnit.DAYS.between(today, reservationDate);

            // 남은 날짜에 따른 환불 정책 적용
            if (daysUntilReservation >= 7) {
                refundAmount = amount;
            } else if (daysUntilReservation >= 4 && daysUntilReservation <= 6) {
                refundAmount = amount / 2;
            } else if (daysUntilReservation >= 0 && daysUntilReservation <= 3) {
                // Toss Payments API 호출을 건너뜀
                System.out.println("0일 ~ 3일 사이에는 환불이 불가능합니다. Toss Payments API 호출을 생략합니다.");
                return "Refund not applicable within 3 days of reservation date.";
            } else {
                throw new IllegalArgumentException("Invalid reservation date. The date is in the past.");
            }
        } else {
            // 예약 날짜가 없을 경우 기본적으로 전액 환불 처리
            System.out.println("예약 날짜가 제공되지 않았습니다. 기본적으로 전액 환불로 처리됩니다.");
        }

        // 요청 본문 설정
        Map<String, Object> body = new HashMap<>();
        body.put("cancelReason", reason);
        body.put("cancelAmount", refundAmount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // Toss Payments API로 POST 요청
            ResponseEntity<String> response = restTemplate.exchange(
                    REFUND_API_URL,
                    HttpMethod.POST,
                    request,
                    String.class,
                    paymentKey
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("Refund failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to refund payment: " + e.getMessage(), e);
        }
    }

    // 결제 승인 로직
    public Map<String, Object> confirmPayment(String paymentKey, String orderId, int amount) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 URL
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic dGVzdF9za182YkpYbWdvMjhlTnliYnFKQWxtWTNMQW5HS1d4Og==");

        // 요청 바디
        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderId);
        body.put("amount", amount);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // API 호출
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), Map.class);
    }


}
