package com.placeHere.server.service.community;

import com.placeHere.server.domain.Community;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface CommunityService {

    //리뷰 INSERT
    public void addReview(Community community) throws Exception;


    //리뷰 상세보기
    public Community getReview(int reviewNo) throws  Exception;


    //전체 리뷰 가져오기
    public List<Community> getReviewList() throws Exception;


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