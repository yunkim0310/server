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
     public void testGetFrinedReq() throws  Exception{

        int friendNo =1;

         Friend friendReq = friendService.getFriendReq(friendNo);

         System.out.println("");
         System.out.println("");
         System.out.println("");
         System.out.println("GetFrinedReq : " + friendReq);
     }


    @Test
    public void testAddFriend() throws Exception{

        int friendNo = 27;

        boolean result = friendService.addFriend(27);

        if (result){
            System.out.println("친구 요청 수락");
        }else {
            System.out.println("친구 요청 거절 ");
        }
    }

//    @Transactional
    // 친구 거절 , 친구신청취소, 친구 삭제는 친구 레코드를 지우는거 동일 -> remove 메서드 하나 만들고 컨트롤러 에서 조절 필요
    @Test
    public void testRemoveFriend() throws Exception{

        friendService.removeFriendReq(27);

        System.out.println("Remove Success !!!");

    }

    @Test
    public void testGetFriendList() throws Exception{
        String friendReq  = "user1";
        int startRowNum = 0;
        int listSize = 3;

        List<Friend> friendList = friendService.getFriendList(friendReq, startRowNum, listSize);


        System.out.println("친구 목록 : " + friendList);


    }


    @Test
    public void testUpdateChkfriendReq() throws Exception{

        friendService.updateChkfriendReq( "user01");



        System.out.println("test===== ::");

    }

}
