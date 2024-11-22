package com.placeHere.server.dao.user;

import com.placeHere.server.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    /// Method
    public User getUser(String username) throws Exception; // 로그인

    // 아이디 중복 확인


    // 회원가입
//    public void


    // 로그인

    // 로그아웃

    // 회원 상태 변경
    // CASE1] (ACTIVE -> INACVTIVE, DELETED)
    // CASE2] (INAVTIVE -> ACTIVE)

    // 내 정보 보기

    // 회원 리스트 보기

    // 회원 상세보기

    // 회원 정보 수정

    //



}
