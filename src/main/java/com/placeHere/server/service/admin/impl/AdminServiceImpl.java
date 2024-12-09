package com.placeHere.server.service.admin.impl;

import com.placeHere.server.dao.admin.AdminDao;
import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.admin.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("adminServiceImpl")

@Transactional()
public class AdminServiceImpl implements AdminService {

    @Autowired
    @Qualifier("adminDao")
    AdminDao adminDao;

    @Override
    public User getUser(long id) throws Exception {

        User user = adminDao.getUser(id);
        return user;
    }

    @Override
    public List<User> getUserList() throws Exception {

        List<User> user = adminDao.getUserList();

        return user;
    }

    @Override
    public List<User> getStoreList() throws Exception {

        List<User> user = adminDao.getStoreList();
        return user;
    }

    @Override
    public List<Reservation> getRsrvList() throws Exception {

        List<Reservation> rsrv = adminDao.getRsrvList();

        return rsrv;
    }

    @Override
    public Reservation getRsrv(int id) throws Exception {

        Reservation rsrv = adminDao.getRsrv(id);
        return rsrv;
    }


}
