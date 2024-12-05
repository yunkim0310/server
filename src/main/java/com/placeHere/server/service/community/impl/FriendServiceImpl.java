package com.placeHere.server.service.community.impl;

import com.placeHere.server.dao.community.FriendDao;
import com.placeHere.server.domain.Friend;
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
        friendDao.sendFriendReq(friend);
    }

    // 친구 신청 확인
    public Friend getFriendReq(int friendNo) throws Exception{
        return friendDao.getFriendReq(friendNo);
    }

    //친구 신청을 수락
    @Override
    public boolean addFriend(int friendNo) throws Exception {
        return friendDao.addFriend(friendNo) ;
    }

    // 친구 신청을 거절 ( 친구신청취소 , 친구삭제 같이 묶음)
    @Override
    public boolean removeFriendReq(int friendNo) throws Exception {

        //친구 요청을 가져옴
        Friend friendRequest = friendDao.getFriendReq(friendNo);

        // 요청이 존재하는지 확인
        if (friendRequest != null) {
            return friendDao.removeFriendReq(friendNo);
        } else {
            throw new IllegalArgumentException("ID에 대한 친구 요청을 찾을 수 없습니다_FriendServiceImpl" + friendNo);
        }
    }


    @Override
    public void removeFriend(int friendNo) throws Exception {
        // 친구를 삭제하는 DAO 메서드 호출
        friendDao.removeFriend(friendNo);
    }
    //친구 목록을 조회하다
        public List<Friend> getFriendList(String friendReq , int startRowNum, int listSize) throws Exception {
        return friendDao.getFriendList(friendReq, startRowNum, listSize);
    }


    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
        public void updateChkfriendReq(String friendRes)throws Exception{
            friendDao.updateChkfriendReq(friendRes);
        }

    //친구 상태 확인
    public boolean chkFriend(String userNameA , String userNameB) throws Exception{
        return friendDao.checkFriendStatus(userNameA, userNameB);
    }

    @Override
    public int getFriendStatus(String currentUser, String targetUser) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("userNameA", currentUser);
        params.put("userNameB", targetUser);
        return friendDao.getFriendStatus(params); // DAO를 통해 친구 상태 조회
    }

    @Override
    public Friend findFriendRelation(Map<String, String> params) throws Exception {
        return friendDao.findFriendRelation(params);
    }





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
