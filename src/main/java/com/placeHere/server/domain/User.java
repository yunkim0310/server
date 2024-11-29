package com.placeHere.server.domain;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    // Field
    // 회원어아디
    private String username;
    // 비밀번호
    private String password;
    // 이메일
    private String email;

    // 역할 (ROLE_USER, ROLE_STORE, ROLE_ADMIN, ROLE_POINT)
    private String role;
    private Date regDt;
    private String profileImg;
    private Date loginDt;
    private Date updateDt;
    private String gender;
    // util의 데이터 타입
    private Date birth;

    // 회원 활동상태
    // ACTIVE : 활동상태
    // INACTIVE : 휴면계정상태
    // DELETED : 탈퇴상태
    private String activeStatus;

}
