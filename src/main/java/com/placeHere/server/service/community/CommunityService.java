package com.placeHere.server.service.community;

import com.placeHere.server.domain.Comment;
import com.placeHere.server.domain.Review;
import com.placeHere.server.domain.Search;

import java.util.List;

public interface CommunityService {

    //리뷰 작성
    public void addReview(Review review) throws Exception;

    //리뷰 상세보기
    public Review getReview(int reviewNo) throws  Exception;

    //전체 리뷰 가져오기
    public List<Review> getReviewList(Search search) throws Exception;

    // 특정 가게의 리뷰 리스트 조회
    public List<Review> getReviewList(int storeId, Search search);

    // 특정 회원들의 리뷰 리스트 조회
    public List<Review> getReviewList(List<String> userNameList, Search search);

    //리뷰 업데이트
    public void updateReview(Review review) throws Exception;

    //리뷰 삭제 -> 노출여부 T => F 로 변경
    public void removeReview(Review review) throws Exception;

    //댓글 작성
    public void addComment(Comment comment) throws Exception;

    //댓글 불러오다
    public List<Comment> getCommentList(int reviewNo) throws Exception;

    //댓글 수정
    public void updateComment(Comment comment) throws Exception;

    //댓글 삭제 -> 노출여부 T => F 로 변경
    public void removeComment(Comment comment) throws Exception;
}