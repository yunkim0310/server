package com.placeHere.server.service.like;

import com.placeHere.server.domain.Like;

import java.util.List;

public interface LikeService {
    // 좋아요 추가
    public void addLike(String userName, int relationNo, String target) throws Exception;

    // 좋아요 취소
    public boolean removeLike(Like like) throws Exception;

    // 좋아요 토탈 카운트
    public Long  totalCount(int relationNo) throws Exception;

    // 좋아요 리스트 ( 인기 가게 => 좋아요가 가장 많은 번호의 리스트 )
    public List<Integer> likeList (String target) throws Exception;

    // 좋아요 검증
    public Like checkLike(Like like) throws Exception;

    // 가게 좋아요 목록 조회
    public List<Like> getStoreLikeList(String userName) throws Exception;

}
