package com.placeHere.server;

import com.placeHere.server.dao.community.FriendDao;
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

    @Autowired
    private FriendDao friendDao;


    @Test
    public void getFriendList() throws Exception {

        List<Friend> friendList = friendService.getFriendList("user01", new Search(5, 5), "");

        System.out.println(friendList);

    }


    @Test
    public void chkFriendByFriendNo() throws Exception {

        Friend chkFriend = friendDao.chkFriendByFriendNo(2);

        System.out.println(chkFriend);

    }

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


    @Test
    public void testremoveFriendRequest() throws Exception{
       int friendNo = 31;

       friendService.removeFriendRequest(friendNo);

    }



}




