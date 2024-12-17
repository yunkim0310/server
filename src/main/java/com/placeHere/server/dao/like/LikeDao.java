package com.placeHere.server.dao.like;

import com.placeHere.server.domain.Like;
import com.placeHere.server.domain.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikeDao {

    // 좋아요 추가
    public void addLike(@Param("userName") String userName, @Param("relationNo") int relationNo, @Param("target") String target) throws Exception;

    // 좋아요 취소
    public void removeLike(Like like) throws Exception;

    // 좋아요 토탈 카운트
    public Long getTotalCount(@Param("relationNo") int relationNo , @Param("target") String target) throws Exception;

    // 좋아요 리스트 ( 인기 가게 => 좋아요가 가장 많은 번호의 리스트 )
    public List<Integer> likeList(String target) throws Exception;

    // 좋아요 검증
    public Like chkLike(Like like) throws Exception;

    // 가게 좋아요 목록 조회
    public List<Like> getStoreLikeList(@Param("userName") String userName,@Param("search") Search search) throws Exception;

}

