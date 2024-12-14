package com.placeHere.server.service.admin;

import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;
import java.util.List;

public interface AdminService {

    /**
     * 회원상세보기
     * @param id
     * @return
     * @throws Exception
     */
    public User getUser(long id) throws Exception;

    /**
     * 일반회원 리스트
     * @return
     * @throws Exception
     */
    public List<User> getUserList() throws Exception;

    /**
     * 점주회원 리스트
     * @return
     * @throws Exception
     */
    public List<User> getStoreList() throws Exception;

    /**
     * 예약 리스트
     * @return
     * @throws Exception
     */
    public List<Reservation> getRsrvList() throws Exception;

    /**
     * 예약 상세보기
     */
    public Reservation getRsrv(int id) throws Exception;





}
