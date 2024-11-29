package com.placeHere.server.dao.community;

import com.placeHere.server.domain.Community;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityDao {

    //리뷰 작성
    public void addReview(Community community) throws Exception;


    //리뷰 상세보기
    public  Community getReview(int reviewNo) throws  Exception;


    //전체 리뷰 가져오기
    public List<Community> getReviewList() throws Exception;

    // 특정 가게의 리뷰 리스트 조회
    public List<Community> getReviewListByStoreId(int storeId);

    // 특정 회원들의 리뷰 리스트 조회
    public List<Community> getReviewListByUserName(@Param("userNameList") List<String> userNameList);


    //리뷰 업데이트
    public void updateReview(Community community) throws Exception;


    //리뷰 삭제 -> 노출여부 T => F 로 변경
    public void removeReview(Community community) throws Exception;

    //댓글 작성
    public void addComment(Community community) throws Exception;

    //댓글 불러오다
    public Community getComment(int commentNo) throws Exception;

    //댓글 수정
    public void updateComment(Community community) throws Exception;

    //댓글 삭제 -> 노출여부 T => F 로 변경
    public void removeComment(Community community) throws Exception;
}
