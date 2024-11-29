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

@SpringBootTest
class FriendServiceTest {

    @Autowired
    @Qualifier("friendServiceImpl")
    private FriendService friendService;

    //친구 신청
//    @Test
    public void testSendFriendReq() throws Exception {
        Friend friend = new Friend();

        friend.setFriendReq("User01");
        friend.setFriendRes("user03");
        friend.setFriendDt(Date.valueOf("2024-11-28"));

        friendService.sendFriendReq(friend);

        System.out.println("====TestFriend===== :::" + friend);

        }

//    @Test
    public void testAddFriend() throws Exception{

        int friendNo = 25;

        boolean result = friendService.addFriend(25);

        if (result){
            System.out.println("친구 요청 수락");
        }else {
            System.out.println("친구 요청 거절 ");
        }
    }

//    @Transactional
//    @Test
    public void testRemoveFriend() throws Exception{

        friendService.removeFriendReq(21);

        System.out.println("Remove Success !!!");

    }

//    @Test
    public void testGetFriendList() throws Exception{


        List<Friend> friendList = friendService.getFriendList("user1");

        System.out.println("friendList : "+ friendList);


    }

    @Test
    public void testCheckFriendReq() throws Exception{

        List<Friend> checkFriendReq = friendService.checkFriendReq("user03");

        System.out.println("checkFriendReq ::: "  + checkFriendReq);
    }

    @Test
    public void testUpdateChkfriendReq() throws Exception{

        friendService.updateChkfriendReq( "user1");



        System.out.println("test===== ::");

    }

}
