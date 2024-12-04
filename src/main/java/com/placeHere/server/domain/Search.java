package com.placeHere.server.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@ToString
public class Search {

    // Field
    // 검색어
    private String searchKeyword = "";
    // 지역필터
    private List<String> regionList;
    // 가격 범위
    private int priceMin;
    private int priceMax;
    // 날짜 범위
    private String startDate;
    private String endDate;
    // 편의시설 목록
    private List<Integer> amenitiesNoList;
    // 해시태그 목록
    private List<String> hashtagList;
    // 음식 카테고리
    private String foodCategoryId;
    // 검색일시
    private LocalDateTime searchDt;
    // 페이징 관련
    private int page;
    // 페이징 사이즈
    private int pageSize;
    // 리스트가 보여지는 개수
    private int listSize;
    // 댓글 사이즈
    private int commentSize;
    // 시작하는 행의 값 (0부터 시작, LIMIT 에 들어가는 값)
    private int startRowNum;
    //예약 상태
    private List<String> searchStatuses;
    // 정렬 기준 (ASC, DESC)
    private String order;


    // Constructor
    public Search() {

        System.out.println("new "+getClass().getSimpleName()+" NoArgsConstructor");

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        startDate = dateFormat.format(today);
        endDate = dateFormat.format(today.plusMonths(1));

        priceMin = 0;
        priceMax = 100000;

        page = 1;
    }

    public Search(int pageSize, int listSize) {

        System.out.println("new "+getClass().getSimpleName()+" Constructor(int, int)");

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        startDate = dateFormat.format(today);
        endDate = dateFormat.format(today.plusMonths(1));

        priceMin = 0;
        priceMax = 100000;

        page = 1;

        this.pageSize = pageSize;
        this.listSize = listSize;

    }


    // Method
    public int getStartRowNum() {
        return (getPage() - 1)*getListSize();
    }

}
