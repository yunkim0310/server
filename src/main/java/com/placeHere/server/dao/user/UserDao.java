package com.placeHere.server.dao.user;

import com.placeHere.server.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    /// Method

    public User getUser(String username) throws Exception;
    // 이메일 중복 확인
    public User chkEmail(String email) throws Exception;

    // 로그인
    // getUser랑 기능 유사하지만 로그인일시 update 때문에 따로 빼둠
    public User login(String username) throws Exception;

    public int join(User user) throws Exception;

    public int updateUserStatus (User user) throws Exception;

    public int updateLoginDt (String username) throws Exception;

    public List<User> getUserList() throws Exception;
    public List<User> getStoreList() throws Exception;

    public boolean resetPwdValidation(User user) throws Exception;

    public int updatePwd(User user) throws Exception;

    public int updateProfile(User user) throws Exception;

}
