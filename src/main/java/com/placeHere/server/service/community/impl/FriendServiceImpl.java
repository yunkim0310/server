package com.placeHere.server.service.community.impl;

import com.placeHere.server.dao.community.FriendDao;
import com.placeHere.server.domain.Friend;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.community.FriendService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Service("FriendServiceImpl")
public class FriendServiceImpl implements FriendService {

    //Field
    @Autowired
    private FriendDao friendDao;
    @Qualifier("friendDao")

    //Method
    //친구 신청을 요청하다
    public void sendFriendReq(Friend friend) throws Exception {

        System.out.println("sendFriendReq");

        friendDao.sendFriendReq(friend);
    }


    // 친구 신청 목록 (내가 friendReq)
    @Override
    public List<Friend> getFriendReqList(String userName, Search search) throws Exception {

        System.out.println("getFriendReqList");

        return friendDao.getFriendReqList(userName, search);
    }


    // 친구 신청 받은 목록 (내가 friendRes)
    @Override
    public List<Friend> getFriendResList(String userName, Search search) throws Exception {

        System.out.println("getFriendResList");

        return friendDao.getFriendResList(userName, search);
    }


    //친구 신청을 수락
    @Override
    public boolean addFriend(int friendNo) throws Exception {

        System.out.println("addFriend 친구 수락");
        
        return friendDao.addFriend(friendNo) ;
    }


    // 친구 신청을 거절 ( 친구신청취소 , 친구삭제 같이 묶음)
    @Override
    public boolean removeFriendReq(Friend friend) throws Exception {

        System.out.println("removeFriendReq 친구 신청 거절");

        return friendDao.removeFriendReq(friend);
    }


    //친구 삭제
    @Override
    public void removeFriend(Friend friend) throws Exception {

        System.out.println("removeFriend :: " + friend.getFriendNo());

        // 친구를 삭제하는 DAO 메서드 호출
        friendDao.removeFriend(friend);
    }


    // 친구 목록 조회
    @Override
    public List<Friend> getFriendList(String userName, Search search) throws Exception {

        System.out.println("getFriendList 친구 목록 조회");

        return friendDao.getFriendList(userName, search);
    }


    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
    public void updateChkfriendReq(String userName)throws Exception{

        System.out.println("updateChkfriendReq 친구 요청 확인");

        friendDao.updateChkfriendReq(userName);
    }


    // 친구 상태 확인 => friend_status =1 만을 구분
    // friendReq 는 내 아이디, friendRes 는 상대 아이디
    public Friend chkFriend(Friend friend) throws Exception {

        System.out.println("chkFriend 친구 상태 확인");

        return friendDao.chkFriend(friend);
    }






//    @Override
//    public int getFriendStatus(String currentUser, String targetUser) throws Exception {
//        Map<String, String> params = new HashMap<>();
//        params.put("userNameA", currentUser);
//        params.put("userNameB", targetUser);
//        return friendDao.getFriendStatus(params); // DAO를 통해 친구 상태 조회
//    }

//    @Override
//    public Friend findFriendRelation(Map<String, String> params) throws Exception {
//        return friendDao.findFriendRelation(params);
//    }




//    // 친구 신청 확인
//    public List<Friend> getFriendReq(String friendReq,int startRowNum, int listSize, String friendRes, String username) throws Exception{
//        return friendDao.getFriendReq(friendReq,startRowNum,  listSize, friendRes, username);
//    }
    //친구 요청을 확인하다   => 필요  X getFriendReq 합침
//        public List<Friend> checkFriendReq(String friendRes) throws Exception{
//            return friendDao.checkFriendReq(friendRes);
//         }

    //    친구 아이디를 검색하다 ( 보류)
//    @Override
//        public List<Friend> searchFriend(Search search) throws Exception{
//        return friendDao.searchFriend(search);
//    }


//    친구 신청을 취소
//    @Override
//        public void cancelFriend(Friend friend) throws Exception{
//        if(!friend.isFriendStatus()){
//            friendDao.removeFriendReq(friend);
//         }
//        }
//

}
