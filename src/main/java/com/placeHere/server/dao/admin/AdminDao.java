package com.placeHere.server.dao.admin;

import com.placeHere.server.domain.Batch;
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

    // 휴면계정전환
    public int userInactive() throws Exception;

    // 예약확정 리스트 가져오기
    public List<Reservation> getRsrvConfirmedList() throws Exception;

    // 예약 상태값 변경 (예약확정 -> 이용완료)
    public int updateServiceComplete() throws Exception;

    public int insertBatchlog(Batch batch) throws Exception;

    public List<Batch> getBatchList() throws Exception;
}
