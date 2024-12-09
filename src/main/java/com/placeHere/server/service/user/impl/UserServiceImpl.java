package com.placeHere.server.service.user.impl;


import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service("userServiceImpl")

@Transactional()
public class UserServiceImpl implements UserService {

    ///Field
    @Autowired
    @Qualifier("userDao")
    UserDao userDao;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder; // BCryptPasswordEncoder 주입

    public void setUserDao(UserDao userDao) {
        System.out.println("::"+getClass()+".setUserDao  Call.....");
        this.userDao = userDao;
    }

    ///Constructor
    public UserServiceImpl() {
        System.out.println("::"+getClass()+" default Constructor Call.....");
    }

    ///Method

    // 중복확인
    public boolean chkDuplication(String username) throws Exception {

        boolean result = false;
        User user = userDao.getUser(username);

        System.out.println("serviceImpl" + user);

        if(user != null) {
            result = true;
        }

        return result;
    }

    // 회원가입
    public void join(User user) throws Exception {

        System.out.println("user 확인 :: " + user);

        // 비밀번호 암호화
//        String rawPassword = user.getPassword();
//        String encodedPassword = bCryptPasswordEncoder.encode(rawPassword); // 암호화

//        user.setPassword(encodedPassword);

        userDao.join(user);

//        System.out.println("chk 11 :: " + rawPassword);
//        System.out.println("chk 11 :: " + encodedPassword);

    }

    @Override
    public User login(String username) throws Exception {
        return null;
    }


    public User getUser(String username) throws Exception {

        int result = 0;
        result = userDao.updateLoginDt(username);

        if (result == 1) {
            return userDao.getUser(username);
        }

        return null;
    }

    @Override
    public void updateUserStatus(String username) throws Exception {

    }

    public int updateLoginDt (String username) throws Exception {

        int result = 0;
        result = userDao.updateLoginDt(username);
        return result;
    }

//    public void updateUserStatus (String username) throws Exception {
//
//        System.out.println("updateUserStatus");
//
//    }

    public List<User> getUserList() throws Exception {

        List<User> user = userDao.getUserList();
        return user;
    }

    public List<User> getStoreList() throws Exception {

        List<User> user = userDao.getStoreList();
        return user;
    }


    @Override
    public boolean resetPwdValidation(User user) throws Exception {

        log.info("resetPwdValidation call ");

        String username = user.getUsername();
        String email = user.getEmail();

        log.info("username :: " + username);
        log.info("email :: " + email);

        boolean result = false;
        result =  userDao.resetPwdValidation(user);

        return result;
    }

    @Override
    public int updatePwd(User user) throws Exception {

        int result = 10;
        result = userDao.updatePwd(user);
        return result;
    }


}
