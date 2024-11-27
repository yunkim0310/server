package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.placeHere.server.domain.User;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Purchase {

    //int 타입의 거래번호
    private int tranNo;
    //String 타입의 회원 아이디
    private User user;
    //
    private String userName;
    //int 타입의 구매한 상품번호
    private Product purchaseProd;
    //
    private int prodNo;
    //int 타입의 거래 관련 번호
    private int relNo;
    //String 타입의 바코드 번호
    private String barcodeNo;
    //String 타입의 바코드이미지이름
    private String barcodeName;
    //int 타입의 거래 포인트
    private int tranPoint;
    //int 타입의 상품 수량
    private int cntProd;
    //Date 타입의 거래 날짜
    private Date pointDt;
    //String 타입의 거래 유형
    private String depType;
    //int 타입의 보유 포인트
    private int currPoint;

    private int wishCartNo;


//    private String prodName;
//    private String prodDetail;
//    private int prodPrice;
//    private String prodImg1;
//    private String prodImg2;
//    private String prodImg3;

}