package com.placeHere.server.dao.community;

import com.placeHere.server.domain.Community;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityDao {

    //리뷰 작성
    public void addReview(Community community) throws Exception;


    //리뷰 상세보기
    public  Community getReview(int reviewNo) throws  Exception;


    //전체 리뷰 가져오기
    public List<Community> getReviewList(Community community) throws Exception;


    //리뷰 업데이트
    public void updateReview(Community community) throws Exception;


    //리뷰 삭제 -> 노출여부 T => F 로 변경
    public void removeReview(Community community) throws Exception;
}
