package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Search {

    // Field
    // 검색어
    private String searchKeyword;
    // 지역필터
    private List<String> regionList;
    // 가격 범위
    private int priceMin;
    private int priceMax;
    // 날짜 범위
    private Date startDate;
    private Date endDate;
    // 편의시설 목록
    private List<Integer> amenitiesList;
    // 해시태그 목록
    private List<String> hashtagList;
    // 음식 카테고리
    private String foodCategory;
    // 검색일시
    private LocalDateTime searchDt;
    // 페이징 관련
    private int page;
    private int pageSize;
    private int listSize;
    private int startRowNum;
    private int endRowNum;
}