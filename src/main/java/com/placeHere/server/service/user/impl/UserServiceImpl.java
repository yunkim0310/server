package com.placeHere.server.service.user.impl;


import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    @Qualifier("userDao")
    UserDao userDao;

    public User getUser(String username) throws Exception {
        return userDao.getUser(username);
    }


}
