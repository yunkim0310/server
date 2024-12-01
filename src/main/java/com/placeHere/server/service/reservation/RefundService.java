package com.placeHere.server.service.reservation;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class RefundService {

    private final String TOSS_SECRET_KEY = "test_sk_6bJXmgo28eNybbqJAlmY3LAnGKWx"; // 테스트 Secret Key
    private final String TOSS_PAYMENT_VERIFY_URL = "https://api.tosspayments.com/v1/payments/";
    private final String REFUND_API_URL = "https://api.tosspayments.com/v1/payments/{paymentKey}/cancel";

    public String refundPayment(String paymentKey, String reason) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(TOSS_SECRET_KEY, ""); // 기본 인증 처리

        // 요청 본문 설정
        Map<String, String> body = new HashMap<>();
        body.put("cancelReason", reason);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

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
                return response.getBody(); // 성공 시 응답 반환
            } else {
                throw new RuntimeException("Refund failed: " + response.getBody());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to refund payment: " + e.getMessage(), e);
        }
    }

    public void verifyAndCompletePayment(String paymentKey, String orderId, int amount) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(TOSS_SECRET_KEY, "");

        // Toss Payments API 호출로 결제 검증
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = TOSS_PAYMENT_VERIFY_URL + paymentKey;

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> paymentData = response.getBody();

            // 결제 검증
            if (!orderId.equals(paymentData.get("orderId")) || amount != (int) paymentData.get("totalAmount")) {
                throw new RuntimeException("OrderId or Amount does not match.");
            }

            System.out.println("결제 검증 성공");


        } else {
            throw new RuntimeException("Payment verification failed: " + response.getBody());
        }
    }


}
