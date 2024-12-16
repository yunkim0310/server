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

    // 리뷰 댓글 목록 조회 - 페이징 X, 리뷰 목록 조회
    public List<Comment> getCommentList(int reviewNo) throws Exception;

    //리뷰의 댓글 목록 조회 - 페이징 O, 리뷰 상세 조회
    public List<Comment> getCommentListBySearch(@Param("reviewNo") int reviewNo, @Param("search") Search search) throws Exception;

    //댓글 수정
    public void updateComment(Comment comment) throws Exception;

    //댓글 삭제 -> 노출여부 T => F 로 변경
    public void removeComment(Comment comment) throws Exception;

    // 댓글 조회 메서드
    public Comment getCommentById(int commentNo);

    //탈퇴 회원이 작성한 모든 리뷰를 삭제 하다.
    public void deleteAllReviewsByUser(String username) throws Exception;

    //탈퇴 회원이 작성한 모든 댓글을 삭제 하다.
    public void deleteAllCommentsByUser(String username) throws Exception;

    //탈퇴 회원의 리뷰 리스트 조회
    public List<Review> getDeletedUserReview(String username) throws Exception;

    //탈퇴 회원의 댓글 리스트 조회
    public List<Comment> getDeletedUserComment(String username) throws Exception;

    // 댓글 목록 조회 - 인기 리뷰용 (reviewNo List 로 조회)
    public List<Review> getReviewListByReviewNo(@Param("reviewNoList") List<Integer> reviewNoList) throws Exception;

}
