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

        Friend chkFriend = friendDao.chkFriend(friend);

        if (chkFriend == null) {
            friendDao.sendFriendReq(friend);
        } else {
            System.out.println("chkFriend = " + chkFriend);
            throw new Exception("친구 신청중 오류 발생");
        }
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

        Friend chkFriend = friendDao.chkFriendByFriendNo(friendNo);

        if (chkFriend != null && !chkFriend.isFriendStatus()) {
            return friendDao.addFriend(friendNo) ;
        } else {
            return false;
        }

    }


    // 친구 신청을 거절 ( 친구신청취소 , 친구삭제 같이 묶음)
    @Override
    public boolean removeFriendReq(Friend friend) throws Exception {

        System.out.println("removeFriendReq 친구 신청 거절");

        Friend chkFriend = friendDao.chkFriendByFriendNo(friend.getFriendNo());

        if (chkFriend != null && !chkFriend.isFriendStatus()) {
            return friendDao.removeFriendReq(friend);
        } else {
            return false;
        }

    }


    //친구 삭제
    @Override
    public void removeFriend(Friend friend) throws Exception {

        System.out.println("removeFriend :: " + friend.getFriendNo());

        Friend chkFriend = friendDao.chkFriendByFriendNo(friend.getFriendNo());

        if (chkFriend != null && chkFriend.isFriendStatus()) {
            friendDao.removeFriend(friend);
        } else {
            System.out.println("chkFriend = " + chkFriend);
            throw new Exception("친구 삭제중 오류 발생");
        }

    }


    // 친구 목록 조회
    @Override
    public List<Friend> getFriendList(String userName, Search search, String keyword) throws Exception {
        System.out.println("getFriendList 친구 목록 조회");
        return friendDao.getFriendList(userName, search, keyword);
    }


    @Override
    public List<String> getFriendList(String userName) {

        return friendDao.getFriendListAll(userName);
    }


    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
    public void updateChkfriendReq(String userName)throws Exception{

        System.out.println("updateChkfriendReq 친구 요청 확인");

        friendDao.updateChkfriendReq(userName);
    }


    // 친구 상태 확인 => friend_status =1 만을 구분
    // friendReq 는 내 아이디, friendRes 는 상대 아이디
    public Friend chkFriend(Friend friend) throws Exception {

        System.out.println("chkFriend 친구 상태 확인" );

        return friendDao.chkFriend(friend);
    }


    //친구 신청 취소
    public void removeFriendRequest(int friendNo)throws Exception{
        System.out.println("removeFriendRequest 친구 신청 취소 ServiceImpl");

        friendDao.removeFriendRequest(friendNo);
    }

    // 친구 정보 가져오는 메서드
    public Friend chkFriendByFriendNo(int friendNo) throws Exception {
        return friendDao.chkFriendByFriendNo(friendNo);
    }
}
