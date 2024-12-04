package com.placeHere.server.service.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final String REFUND_API_URL = "https://api.tosspayments.com/v1/payments/{paymentKey}/cancel";

    public String refundPayment(String paymentKey, String reason) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic dGVzdF9za182YkpYbWdvMjhlTnliYnFKQWxtWTNMQW5HS1d4Og==");

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

    public Map<String, Object> confirmPayment(String paymentKey, String orderId, int amount) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 URL
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic dGVzdF9za182YkpYbWdvMjhlTnliYnFKQWxtWTNMQW5HS1d4Og=="); // Base64 인코딩된 시크릿 키

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
        return objectMapper.readValue(response.getBody(), Map.class); // Map으로 변환
    }


}