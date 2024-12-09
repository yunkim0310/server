package com.placeHere.server.service.user;

import com.placeHere.server.domain.User;

import java.util.List;


public interface UserService {

    // 아이디 중복확인
    public boolean chkDuplication(String username) throws Exception;

    // 회원가입
    public void join(User user) throws Exception;

    // 로그인
    // getUser랑 기능 유사하지만 로그인일시 update 때문에 따로 빼둠
    public User login(String username) throws Exception;

    public User getUser(String username) throws Exception;

//    public void updateUserStatus (String username) throws Exception;

    void updateUserStatus(String username) throws Exception;

    public int updateLoginDt (String username) throws Exception;

    public List<User> getUserList() throws Exception;

    public List<User> getStoreList() throws Exception;

    public boolean resetPwdValidation(User user) throws Exception;

    public int updatePwd(User user) throws Exception;
}
