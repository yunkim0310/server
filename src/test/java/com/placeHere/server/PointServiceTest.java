//package com.placeHere.server;
//
//import com.placeHere.server.dao.pointShop.PointDao;
//import com.placeHere.server.domain.Point;
//import com.placeHere.server.domain.User;
//import com.placeHere.server.service.pointShop.PointService;
//import com.placeHere.server.service.pointShop.PurchaseService;
//import com.placeHere.server.service.user.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@SpringBootTest
//public class PointServiceTest {
//
//    @Autowired
//    @Qualifier("pointServiceImpl")
//    private PointService pointService;
//
//    @Autowired
//    @Qualifier("pointDao")
//    private PointDao pointDao;
//
//    @Autowired
//    @Qualifier("userServiceImpl")
//    private UserService userService;
//
//    //    @Transactional
//    @Test
//    public void testAddPointTransaction() throws Exception {
//
//        pointService.addPointTransaction("user1", -1000, "상품 구매", 21200, 1);
//
//        List<Point> pointTransactionList = pointService.getPointHistoryList("user1");
//
//        System.out.println("AddPointTransaction Success!!");
//
//
//    }
//
////    @Test
////    public void testUpdatePoint() throws Exception {
////
//////        Point point = new Point();
////
////        String username = "user1";
////
////        int totalPoint = pointService.getCurrentPoint(username);
////        pointService.updatePoint("user1", -1000);
////
////        int updatedPoint = pointService.getCurrentPoint(username);
////
////        Assertions.assertEquals(totalPoint - 1000, updatedPoint);
////
////        System.out.println("updatedPoint : " + updatedPoint);
////
////    }
//
//    @Test
//    public void testGetCurrentPoint() throws Exception {
//
//        int currPoint = pointService.getCurrentPoint("user1");
//
//        System.out.println("currPoint : " + currPoint);
//
//        Assertions.assertEquals(3300, currPoint);
//
//    }
//
//    @Test
//    public void testGetPointHistoryList() throws Exception {
//
//        List<Point> pointHistoryList = pointService.getPointHistoryList("user1");
//
//        System.out.println("pointHistoryList : " + pointHistoryList);
//    }
//}