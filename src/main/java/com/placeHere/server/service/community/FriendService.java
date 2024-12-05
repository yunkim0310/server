package com.placeHere.server.service.community;

import com.placeHere.server.domain.Friend;

import java.util.List;
import java.util.Map;

public interface FriendService {

    //친구 신청을 요청하다
    public void sendFriendReq(Friend friend) throws Exception;

    // 친구 신청 확인
    public Friend getFriendReq(int friendNo)throws  Exception;

    //친구 신청을 수락하다
    public boolean addFriend(int friendNo) throws  Exception;

    // 친구 신청을 거절하다 (친구신청취소 , 친구삭제 같이 묶음)
    public boolean removeFriendReq(int friendNo) throws Exception;

    //친구 신청을 취소하다
//    public void cancelFriend(Friend friend) throws Exception;

//    //친구를 삭제하다
    public void removeFriend(int friendNo) throws Exception;

//    //친구 아이디를 검색하다 ( 보류)
//    public List<Friend> searchFriend(Search search) throws Exception;
//
    //친구 목록을 조회하다
    public List<Friend> getFriendList(String friendReq,int startRowNum, int listSize) throws Exception;

    //친구 요청을 확인하다   => 필요  X getFriendReq 합침
//    public List<Friend> checkFriendReq(String friendRes) throws Exception;

    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
    public void updateChkfriendReq(String friendRes) throws Exception;

    // 친구 상태 확인
    public boolean chkFriend(String userNameA , String userNameB) throws Exception;

    // 친구 상태 확인 메소드 추가
    public int getFriendStatus(String currentUser, String targetUser) throws Exception;

    // 친구 관계 조회 메소드 추가
    public Friend findFriendRelation(Map<String, String> params) throws Exception;
}