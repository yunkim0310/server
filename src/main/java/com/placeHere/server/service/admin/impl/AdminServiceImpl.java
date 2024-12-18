package com.placeHere.server.service.admin.impl;

import com.placeHere.server.dao.admin.AdminDao;
import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.Batch;
import com.placeHere.server.domain.Point;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.admin.AdminService;
import com.placeHere.server.service.pointShop.PointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service("adminServiceImpl")
@Transactional()
public class AdminServiceImpl implements AdminService {

    @Autowired
    @Qualifier("adminDao")
    AdminDao adminDao;

    @Autowired
    @Qualifier("pointServiceImpl")
    PointService pointService;

    private Set<Integer> processedRsrvNos = new HashSet<>();

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

    /**
     * 예약상태값 변경 스케줄러
     * 예약 확정 -> 이용 완료
     * @throws Exception
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void updateServiceComplete() throws Exception {

        // 1. 예약상태 update 예약 확정 -> 이용 완료
        int statusResult = 0;

        Batch batch = new Batch();
        batch.setBatchName("예약상태변경");
        batch.setExec_dt(new Date());

//        Set<Integer> processedRsrvNos = new HashSet<>();
        
        try {
            statusResult = adminDao.updateServiceComplete();

            // 2. 예약 상태 변경 로그 찍기
            log.info( statusResult + " 건의 예약을 이용완료로 변경했습니다.");
            batch.setStatus("SUCCESS");
    
            ///////////// 포인트 적립 /////////////////////////
    
            // 1. 이용완료 예약 정보 리스트 가져오기
            List<Reservation> getRsrvConfirmedList = adminDao.getRsrvConfirmedList();
    
            if ( getRsrvConfirmedList != null ) {
    
                for (Reservation rsrv : getRsrvConfirmedList) {
    
                    Point point = new Point();
                    int rsrvNo = rsrv.getRsrvNo();

                    // rsrvNo가 이미 처리된 경우, skip
                    if (processedRsrvNos.contains(rsrvNo)) {
//                        log.info("이미 처리된 예약입니다. rsrvNo: " + rsrvNo);
                        continue; // 중복된 예약은 건너뜁니다.
                    }

                    String username = rsrv.getUserName();
                    int tranPoint = 600;
                    String depType = "예약 이용 완료";
                    int currPoint = pointService.getCurrentPoint(username);
                    point.setCurrPoint(currPoint);
                    point.setTranPoint(tranPoint);
                    point.setDepType(depType);
                    point.setUsername(username);
                    point.setRelNo(rsrvNo);
    
                    pointService.addPointTransaction(username, tranPoint, depType, currPoint, rsrvNo );
    
                    pointService.updatePoint(point);

                    processedRsrvNos.add(rsrvNo);
    
                }
            } // end of if
        } catch (Exception e) {
            batch.setStatus("FAIL");
            throw new RuntimeException(e);
        }
        // 배치이력
        adminDao.insertBatchlog(batch);
    }

//    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    // 매일 03시 진행
    @Scheduled(cron = "0 0 3 * * ?")
    public void userInactive() throws Exception {

        Batch batch = new Batch();
        batch.setBatchName("휴면계정전환");
        batch.setExec_dt(new Date());

        // 1. 상태값 update ACTIVE -> INACTIVE
        int result = 0;
        try {
            // 휴면계정전환
            result = adminDao.userInactive();
            log.info( result + "개의 계정이 비활성화 되었습니다.");

            batch.setStatus("SUCCESS");

        } catch (Exception e) {
            batch.setStatus("FAIL");
            throw new RuntimeException(e);
        }
        // 배치이력
            log.info("batch domain :: " + batch);
        adminDao.insertBatchlog(batch);

    }

    public List<Batch> getBatchList() throws Exception{
        return adminDao.getBatchList();
    }


}
