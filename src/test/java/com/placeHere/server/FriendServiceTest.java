package com.placeHere.server;

import com.placeHere.server.domain.Friend;
import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.community.FriendService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class FriendServiceTest {

    @Autowired
    @Qualifier("FriendServiceImpl")
    private FriendService friendService;

    //친구 신청
    @Test
    public void testSendFriendReq() throws Exception {
        Friend friend = new Friend();

        friend.setFriendReq("User01");
        friend.setFriendRes("user03");
        friend.setFriendDt(Date.valueOf("2024-11-28"));

        friendService.sendFriendReq(friend);

        System.out.println("====TestFriend===== :::" + friend);

        }

    @Test
    public void testUpdateChkfriendReq() throws Exception{

        friendService.updateChkfriendReq( "user01");

        System.out.println("test===== ::");

    }

    // 친구 요청 목록 테스트
    @Test
    public void testGetFriendReqList() throws Exception {
        String friendReq = "user01"; // 친구 요청을 받는 사용자

        // Search 객체 초기화
        Search search = new Search();
        search.setListSize(10); // 요청 목록 크기 설정
        search.setStartRowNum(0); // 시작 행 설정

        // 실제 서비스 호출
        List<Friend> friendRequests = friendService.getFriendReqList(friendReq, search);

        // 검증
        assertNotNull(friendRequests, "친구 요청 목록이 null이면 안 됩니다."); // 결과가 null이 아님을 확인
        assertFalse(friendRequests.isEmpty(), "친구 요청 목록이 비어있지 않아야 합니다."); // 요청 목록이 비어있지 않은지 확인

        // 요청 목록의 세부 정보 출력
        for (Friend request : friendRequests) {
            // 보낸 사람, 받는 사람, 친구 요청 날짜 등을 출력
            System.out.println("보낸 사람: " + request.getFriendReq());
            System.out.println("받는 사람: " + request.getFriendRes());
            System.out.println("친구 신청 날짜: " + request.getFriendDt());
        }
    }

    @Test
    public void testGetFriendResList() throws Exception {
        String userName = "user01"; // 친구 요청을 수신하는 사용자
        Search search = new Search(); // 검색 조건 초기화
        search.setListSize(10); // 요청 목록 크기 설정
        search.setStartRowNum(0); // 시작 행 설정

        // 친구 요청 목록을 가져오는 실제 서비스 호출
        List<Friend> friendResponses = friendService.getFriendResList(userName, search);

        // 검증
        assertNotNull(friendResponses, "친구 신청 받은 목록이 null이면 안 됩니다."); // 결과가 null이 아님을 확인
        assertFalse(friendResponses.isEmpty(), "친구 신청 받은 목록이 비어있지 않아야 합니다."); // 요청 목록이 비어있지 않은지 확인

        // 요청 목록의 세부 정보 출력
        for (Friend response : friendResponses) {
            System.out.println("보낸 사람: " + response.getFriendReq());
            System.out.println("받는 사람: " + response.getFriendRes());
            System.out.println("친구 신청 날짜: " + response.getFriendDt());
        }
    }


//    @Test
//    public void testGetFriendRes() throws Exception {
//        String friendRes = "user02"; // 친구 요청을 보낸 사용자
//        Search search = new Search(); // 필요에 따라 검색 조건 설정
//
//        // 실제 서비스 메서드를 호출하여 친구 신청 받은 목록 요청
//        List<Friend> friendResponses = friendService.getFriendResList(friendRes, search);
//
//        // 검증
//        assertNotNull(friendResponses); // 결과가 null이 아님을 확인
//        assertFalse(friendResponses.isEmpty(), "친구 신청 받은 목록이 비어있지 않아야 합니다."); // 목록이 비어있지 않은지 확인
//
//        // 결과 출력
//        for (Friend response : friendResponses) {
//            System.out.println("보낸 사람: " + response.getFriendReq());
//        }
//    }




}



//     @Test
//     public void testGetFrinedReq() throws  Exception{
//
//        String friendReq = "user01";
//
//         List<Friend> friendReqest = friendService.getFriendReq(friendReq);
//
//         System.out.println("");
//         System.out.println("");
//         System.out.println("");
//         System.out.println("GetFrinedReq : " + friendReq);
//         System.out.println("asdasdasd" + friendService.getFriendReq("user01"));
//         System.out.println("");
//         System.out.println("");
//         System.out.println("");
//     }




//    @Transactional
// 친구 거절 , 친구신청취소, 친구 삭제는 친구 레코드를 지우는거 동일 -> remove 메서드 하나 만들고 컨트롤러 에서 조절 필요
//    @Test
//    public void testRemoveFriend() throws Exception{
//
////        friendService.removeFriend(4);
//
//
//        System.out.println("Remove Success !!!");
//
//    }

//    @Test
//    public void testGetFriendList() throws Exception{
//        String friendReq = "user01";
//        String friendRes = "user01";
//        int startRowNum = 0;
//        int listSize = 10;
//        String username = "user01";
//
//        List<Friend> friendList = friendService.getFriendList(friendReq, startRowNum, listSize, friendRes, username);
//
//        System.out.println("friendReq1111: " + friendReq);
//        System.out.println("friendRes22222: " + friendRes);
//        System.out.println("username333333: " + username);
//        System.out.println("친구 목록 444444: " + friendList);
//
//
////        assertNotNull(friendList);
////        assertFalse(friendList.isEmpty());
//
//        System.out.println("친구 목록 2342342: " + friendList);
//
//        for (Friend friend : friendList) {
//            assertTrue(friend.isFriendStatus(), "친구 상태가 1이어야 합니다.");
//        }
//
//    }