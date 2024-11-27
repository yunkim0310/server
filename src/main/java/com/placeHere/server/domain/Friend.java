package com. placeHere. server. domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Friend {

    // Field 친구
    // 친구 번호
    private int friendNo;
    //친구 수락 여부
    private boolean friendStatus;
    //친구 신청 받은 아이디
    private String friendRes;
    // 친구 신청 날짜
    private Date friendDate;
    //친구 요청 확인 여부
    private boolean chkfriendReq;
}
