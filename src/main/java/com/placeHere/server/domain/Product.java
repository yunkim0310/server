package com.placeHere.server.domain;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {

    //int 타입의 상품번호
    private int prodNo;
    //String 타입의 상품명
    private String prodName;
    //String 타입의 상품 상세 정보
    private String prodDetail;
    //int 타입의 상품 가격
    private int prodPrice;
    //String 타입의 포인트 상품 이미지1 파일 이름
    private String prodImg1;
    //String 타입의 포인트 상품 이미지1 파일 이름
    private String prodImg2;
    //String 타입의 포인트 상품 이미지1 파일 이름
    private String prodImg3;
    //int 타입의 상품 카테고리 번호
    private int prodCateNo;
    //String 타입의 상품 카테고리명
    private String prodCateName;
    //java.sql.Date 타입의 포인트 상품 등록일
    private Date regDt;
    //boolean 타입의 판매 가능 상태
    private boolean prodStatus;


    //private String regDtString;

}
