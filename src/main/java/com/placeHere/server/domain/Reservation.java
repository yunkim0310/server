package com.placeHere.server.domain;

import lombok.Data;
import java.util.Date;

@Data
public class Reservation {

    // Field
    // 예약 번호 (PK)
    private int rsrvNo;
    // 가게 ID
    private int storeId;
    // 회원 이름
    private String userName;
    // 예약 상태
    private String rsrvStatus;
    // 예약 일시
    private Date rsrvDt;
    // 예약 인수
    private int rsrvPerson;
    // 가격
    private int amount;
    // 결제 고유 ID
    private String paymentId;
    // 요청 사항
    private String rsrvReq;
    // 사유(환불)
    private String reason;
    // 예약 생성 시각
    private Date rsrvCreateTime;
    // 가게 이름
    private String storeName;
    // 가게 주소
    private String storeAddr;
    // 예약 전화 번호
    private String rsrvNumber;
    // 스토어 정보
    private Store store;
    // 총 페이지
    private int totalCnt;

}
