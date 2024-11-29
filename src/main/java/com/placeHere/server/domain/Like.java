package com.placeHere.server.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class Like {

    // Field 좋아요
    // 좋아요 번호
    private int likeId;
    //좋아요 한 user
    private String userName;
    // 좋아요 등록 날짜
    private Date likeDt;
    // 관련 번호
    private int relationNo;
    // 좋아요 대상
    private String target;
    // 매장명
    private String storeName;



    public Like(String userName){
        this.userName = userName;
    }

    public Like(int relationNo){
        this.relationNo = relationNo;
    }

}