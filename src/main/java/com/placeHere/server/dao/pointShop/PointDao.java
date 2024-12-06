package com.placeHere.server.dao.pointShop;

import com.placeHere.server.domain.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointDao {

    // 포인트 내역 추가
    public void addPointTransaction(@Param("username") String username,
                                    @Param("tranPoint") int tranPoint,
                                    @Param("depType") String depType,
                                    @Param("currPoint") int currPoint,
                                    @Param("relNo") Integer relNo);

    // 보유 포인트 업데이트
    public void updatePoint(@Param("username") String username,
                            @Param("tranPoint") int tranPoint);

    // 현재 보유 포인트 조회
    public int getCurrentPoint(@Param("username") String username);

    // 포인트 내역 조회
    public List<Point> getPointHistoryList(String username);

}