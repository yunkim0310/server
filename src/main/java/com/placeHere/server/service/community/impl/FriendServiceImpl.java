package com.placeHere.server.service.community.impl;

import com.placeHere.server.dao.community.FriendDao;
import com.placeHere.server.domain.Friend;
import com.placeHere.server.service.community.FriendService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Service("friendServiceImpl")
public class FriendServiceImpl implements FriendService {

    //Field
    @Autowired
    private FriendDao friendDao;
    @Qualifier("friendDao")

    //Method
    //친구 신청
    public void sendFriendReq(Friend friend) throws Exception {
        friendDao.sendFriendReq(friend);
    }

//    친구 신청을 수락
    @Override
    public boolean addFriend(int friendNo) throws Exception {
        try {
            friendDao.addFriend(friendNo);
            return true;
        } catch (Exception e){
            return false;
        }

    }


    // 친구 신청을 거절 ( 친구신청취소 , 친구삭제 같이 묶음)
    @Override
        public void removeFriendReq(int friendNo) throws Exception {
        friendDao.removeFriendReq(friendNo);
    }
//
//    //친구 신청을 취소
//    @Override
//        public void cancelFriend(Friend friend) throws Exception{
//        if(!friend.isFriendStatus()){
//            friendDao.removeFriendReq(friend);
//         }
//        }
//
//    //친구를 삭제하다
//    @Override
//         public void removeFriend(Friend friend) throws Exception{
//        if(friend.isFriendStatus()){
//            friendDao.removeFriend(friend);
//        }
//    }

//    //친구 아이디를 검색하다 ( 보류)
//    @Override
//        public List<Friend> searchFriend(Search search) throws Exception{
//        return friendDao.searchFriend(search);
//    }
//    //친구 목록을 조회하다
        public List<Friend> getFriendList(String friendReq) throws Exception {
        return friendDao.getFriendList(friendReq);

    }

//    //친구 요청을 확인하다   (친구요청현황 확인)
        public List<Friend> checkFriendReq(String friendRes) throws Exception{
            return friendDao.checkFriendReq(friendRes);
         }

    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
        public void updateChkfriendReq(String friendRes)throws Exception{
            friendDao.updateChkfriendReq(friendRes);
        }


}
