package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class StoreOperation {

    // Field
    // 가게 운영 ID (PK)
    private int operationId;
    // 가게 ID (FK)
    private int storeId;
    // 오픈 시간
    private Time openTime;
    // 마감 시간
    private Time closeTime;
    // 브레이크 타임
    private Time breakTimeStart;
    private Time breakTimeEnd;
    // 정기 휴무요일(최대3개)
    private List<String> regularClosedayList;
    private String regularCloseday1;
    private String regularCloseday2;
    private String regularCloseday3;
    // 예약 보증금(5000~10000원, 1000원 단위)
    private int security;
    // 예약 최대 인수
    private int rsrvLimit;
    // 적용일
    private Date effectDt;
    // 휴무일 목록
    private List<Date> closedayList;


    // Method
    public void setRegularClosedayList(List<String> regularClosedayList) {
        
        // 정기 휴무요일이 3개가 아니면 List 의 남는 부분을 null 로 넣게끔함
        regularClosedayList = (regularClosedayList == null) ? new ArrayList<>() : regularClosedayList;
        int regularClosedayCnt = regularClosedayList.size();

        if (regularClosedayCnt < 3) {

            while (regularClosedayCnt < 3) {

                regularClosedayList.add(null);
            }

        }

        this.regularClosedayList = regularClosedayList;
    }
}
