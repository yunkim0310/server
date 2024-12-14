package com.placeHere.server.service.admin.impl;

import com.placeHere.server.dao.admin.AdminDao;
import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.admin.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Date;
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

//    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    // 매일 03시 진행
    @Scheduled(cron = "0 0 3 * * ?")
    public void userInactive() throws Exception {

        // 1. 로그인 일시가 365가 지난 회원 조회

        // 2. 상태값 update ACTIVE -> INACTIVE
        int result = adminDao.userInactive();
        log.info("10초 후 실행 => time : " + LocalTime.now());

        // 3. 상태값 변경한 회원 로그 찍기
        log.info( result + "개의 계정이 비활성화 되었습니다.");
    }



}
