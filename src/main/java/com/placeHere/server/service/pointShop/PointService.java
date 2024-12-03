package com.placeHere.server.service.pointShop;

import com.placeHere.server.domain.Point;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PointService {

    // 포인트 내역 추가
    public void addPointTransaction(@Param("userName") String userName,
                                    @Param("tranPoint") int tranPoint,
                                    @Param("depType") String depType,
                                    @Param("currPoint") int currPoint,
                                    @Param("relNo") Integer relNo);

    // 보유 포인트 업데이트
    public void updatePoint(@Param("userName") String userName,
                            @Param("tranPoint") int tranPoint);

    // 현재 보유 포인트 조회
    public int getCurrentPoint(String userName);

    // 포인트 내역 조회
    public List<Point> getPointHistoryList(String userName);
}