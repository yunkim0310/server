package com.placeHere.server.dao.admin;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDao {

    // 일반회원 상세
    public User getUser(long id) throws Exception;

    // 일반회원 리스트
    public List<User> getUserList() throws Exception;

    // 점주회원 리스트
    public List<User> getStoreList() throws Exception;

    // 예약 리스트
    public List<Reservation> getRsrvList() throws Exception;

    // 예약 상세
    public Reservation getRsrv(int id) throws Exception;
}
