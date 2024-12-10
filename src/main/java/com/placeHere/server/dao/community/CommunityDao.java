package com.placeHere.server.dao.community;

import com.placeHere.server.domain.Comment;
import com.placeHere.server.domain.Review;
import com.placeHere.server.domain.Search;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityDao {

    //리뷰 작성
    public void addReview(Review review) throws Exception;

    //리뷰 상세보기
    public Review getReview(int reviewNo) throws  Exception;

    //전체 리뷰 가져오기
    public List<Review> getReviewList(Search search) throws Exception;

    // 특정 가게의 리뷰 리스트 조회
    public List<Review> getReviewListByStoreId(@Param("storeId") int storeId, @Param("search") Search search);

    // 특정 회원들의 리뷰 리스트 조회
    public List<Review> getReviewListByUserName(@Param("userNameList") List<String> userNameList, @Param("search") Search search);

    //리뷰 업데이트
    public void updateReview(Review review) throws Exception;

    //리뷰 삭제 -> 노출여부 T => F 로 변경
    public void removeReview(Review review) throws Exception;

    //댓글 작성
    public void addComment(Comment comment) throws Exception;

    //리뷰의 댓글 목록 불러오다
    public List<Comment> getCommentList(@Param("reviewNo") int reviewNo) throws Exception;
//    public List<Comment> getCommentList(@Param("reviewNo") int reviewNo, @Param("search") Search search) throws Exception;

    //댓글 수정
    public void updateComment(Comment comment) throws Exception;

    //댓글 삭제 -> 노출여부 T => F 로 변경
    public void removeComment(Comment comment) throws Exception;

    // 댓글 조회 메서드
    public Comment getCommentById(int commentNo);
}
