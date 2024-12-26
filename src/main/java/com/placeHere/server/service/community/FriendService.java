package com.placeHere.server.service.community;

import com.placeHere.server.domain.Friend;
import com.placeHere.server.domain.Search;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FriendService {

    //친구 신청을 요청하다
    public void sendFriendReq(Friend friend) throws Exception;

    // 친구 신청 목록 (내가 friendReq)
    public List<Friend> getFriendReqList(String userName, Search search) throws Exception;

    // 친구 신청 받은 목록 (내가 friendRes)
    public List<Friend> getFriendResList(String userName, Search search) throws Exception;

    //친구 신청을 수락하다 // 친구 수락 시 받아올 필요 없어서 void
    public boolean addFriend(int friendNo) throws Exception;

    //친구 신청을 거절하다 (친구신청취소 , 친구삭제 같이 묶음)
    public boolean removeFriendReq(Friend friend) throws Exception;

    //친구를 삭제하다
    public void removeFriend(Friend friend) throws Exception;

    //친구 목록을 조회하다 TODO
    public List<Friend> getFriendList(String userName, Search search, String keyword) throws Exception;

    // 친구 목록 조회 (친구 리뷰용)
    public List<String> getFriendList(String userName);

    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
    public void updateChkfriendReq(String userName) throws Exception;

    // 친구 상태 확인 => friend_status =1 만을 구분 TODO
    // friendReq 는 내 아이디, friendRes 는 상대 아이디
    public Friend chkFriend(Friend friend) throws Exception;

    //친구 신청 취소
    public void removeFriendRequest(int friendNo) throws Exception;

    // 친구 정보 가져오는 메서드
    public Friend chkFriendByFriendNo(int friendNo) throws Exception;

}