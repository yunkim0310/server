package com.placeHere.server.dao.community;

import com.placeHere.server.domain.Friend;
import com.placeHere.server.domain.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FriendDao {

    //친구 신청을 요청하다
    public void sendFriendReq(Friend friend) throws Exception;

    // 친구 신청 목록 (내가 friendReq)
    public List<Friend> getFriendReqList(@Param("userName") String userName, @Param("search") Search search) throws Exception;

    // 친구 신청 받은 목록 (내가 friendRes)
    public List<Friend> getFriendResList(@Param("userName") String userName, @Param("search") Search search) throws Exception;

    //친구 신청을 수락하다 // 친구 수락 시 받아올 필요 없어서 void
    public boolean addFriend(int friendNo) throws Exception;

    //친구 신청을 거절하다 (친구신청취소 , 친구삭제 같이 묶음)
    public boolean removeFriendReq(Friend friend) throws Exception;

    //친구를 삭제하다
    public void removeFriend(Friend friend) throws Exception;

    //친구 목록을 조회하다 TODO
    public List<Friend> getFriendList(@Param("userName") String userName,@Param("search") Search search) throws Exception;

    // 친구 요청 확인시 체크상태 변경 ( chkfriend_req  0 -> 1로 변경 )
    public void updateChkfriendReq(String userName) throws Exception;

    // 친구 상태 확인 => friend_status =1 만을 구분 TODO
    // friendReq 는 내 아이디, friendRes 는 상대 아이디
    public Friend chkFriend(Friend friend);



    // 친구 상태 확인
//    public boolean checkFriendStatus(String userNameA, String userNameB) throws Exception;

//    //두 사용자의 친구 관계 조회 메소드 추가
//    Friend findFriendRelation(Map<String, String> params);
//
//    // 친구 상태 확인
//    public int getFriendStatus(Map<String, String> params) throws Exception;
//
//    List<Friend> getFriendReq(String friendRes);

    // 친구 요청을 확인하다   => 필요  X getFriendReq 합침
    //public List<Friend> checkFriendReq(String friendRes) throws Exception;

    // 친구 신청을 취소하다
    //public void cancelFriend(Friend friend) throws Exception;

    // 친구 아이디를 검색하다 ( 보류 )
    // public List<Friend> searchFriend(Search search) throws Exception;

    }