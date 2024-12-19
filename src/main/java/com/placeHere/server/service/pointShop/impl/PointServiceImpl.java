package com.placeHere.server.service.pointShop.impl;

import com.placeHere.server.dao.pointShop.PointDao;
import com.placeHere.server.domain.Point;
import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.PointService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service("pointServiceImpl")
public class PointServiceImpl implements PointService {

    @Autowired
    @Qualifier("pointDao")
    private PointDao pointDao;

    // 포인트 내역 추가
    @Override
    public void addPointTransaction(@Param("username") String username,
                                    @Param("tranPoint") int tranPoint,
                                    @Param("depType") String depType,
                                    @Param("currPoint")  int currPoint,
                                    @Param("relNo") Integer relNo) {

        pointDao.addPointTransaction(username, tranPoint, depType, currPoint, relNo);
    }

    // 보유 포인트 업데이트
    @Override
    public void updatePoint(Point point) {

        point.setTranPoint(Integer.parseInt(String.valueOf(point.getTranPoint())));
//        System.out.println("tranPoint : "+point.getTranPoint());

        pointDao.updatePoint(point);
    }

    // 현재 보유 포인트 조회
    @Override
    public int getCurrentPoint(@Param("username") String username) {

        return pointDao.getCurrentPoint(username);
    }

    // 포인트 내역 조회
//    @Override
//    public List<Point> getPointHistoryList(@Param("username") String username) {
//
//        return pointDao.getPointHistoryList(username);
//    }

    public List<Point> getPointHistoryList(Search search) {

        return pointDao.getPointHistoryList(search);

    }

    public int isTransactionExist(int relNo) throws Exception {
        int count = pointDao.isTransactionExist(relNo);
        return count;
    }

}