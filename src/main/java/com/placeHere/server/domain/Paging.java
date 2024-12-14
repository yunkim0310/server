package com.placeHere.server.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Paging {

    // Field
    // 페이지바 시작페이지
    private int startPage;
    // 페이지바 끝 페이지
    private int endPage;
    // 게시물양
    private int totalCnt;
    // 전체 페이지양
    private int totalPage;
    // 현재 페이지
    private int page;
    // 왼쪽 페이지 여부
    private boolean isLeft;
    // 오른쪽 페이지 여부
    private boolean isRight;
    // 페이지바에 보여질 페이지 최대 개수
    private int pageSize;
    // 페이지당 보여줄 리스트 행의 개수
    private int listSize;


    // Constructor
    public Paging() {

        startPage = 1;
        endPage = 1;

    }

    public Paging(int pageSize, int listSize) {

        startPage = 1;
        endPage = 1;

        this.pageSize = pageSize;
        this.listSize = listSize;

    }

    public Paging(int totalCnt, int page, int pageSize, int listSize) {

        super();

        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;
        this.listSize = listSize;

        calcPage(totalCnt, page);

    }


    // Method
    public void calcPage(int totalCnt, int page) {

        System.out.printf("Paging().calcPage(totalCnt=%d, page=%d)%n", totalCnt, page);

        this.totalCnt = totalCnt;
        this.page = (page == 0)? 1 : page;

        totalPage = 1;
        startPage = 1;
        endPage = 1;

        if (totalCnt > 0) {

            totalPage = (int) Math.ceil(totalCnt * 1.0 / listSize);
            System.out.println("totalPage = " + totalPage);

        }

        if (totalPage > 0) {

            endPage = (int) Math.ceil(page * 1.0 / pageSize) * pageSize;

            if (endPage > totalPage) {

                endPage = totalPage;

            }

            startPage = (int) (Math.ceil(page * 1.0 / pageSize) - 1) * pageSize + 1;

            isLeft = page > pageSize;
            isRight = endPage < totalPage;

        }

        System.out.printf("startPage : %d, endPage : %d%n", startPage, endPage);
        System.out.printf("isLeft: %b, isRight: %b%n", isLeft, isRight);

    }

}
