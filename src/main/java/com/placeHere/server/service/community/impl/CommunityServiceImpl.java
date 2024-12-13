package com.placeHere.server.service.community.impl;

import com.placeHere.server.dao.community.CommunityDao;
import com.placeHere.server.domain.Comment;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.domain.Review;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.reservation.ReservationService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Service("communityServiceImpl")
public class CommunityServiceImpl implements CommunityService {

    // Field
    @Autowired
    @Qualifier("communityDao")
    private CommunityDao communityDao;

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService reservationService;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    @Value("${comment_size}")
    private int commentSize;


    // Constructor
    public CommunityServiceImpl(){
    }


    // Method
    //리뷰 작성
    @Override
    public void addReview(Review review) throws Exception {

        System.out.println("addReview(review)");

        communityDao.addReview(review);

        int rsrvNo = review.getRsrvNo();
        System.out.println("rsrvNo : "+rsrvNo);



    }


    //리뷰 상세보기
    @Override
    public Review getReview(int reviewNo) throws Exception {

        System.out.println("getReview(reviewNo)");

        Review review = communityDao.getReview(reviewNo);
        review.setCommentList(communityDao.getCommentList(review.getReviewNo()));

        return review;
    }


    //전체 리뷰 가져오기
    @Override
    public List<Review> getReviewList(Search search) throws Exception {

        System.out.println("getReviewList(search) : 전체 리뷰 목록 조회");

        return communityDao.getReviewList(search);
    }


    // 특정 가게의 리뷰 리스트 조회
    @Override
    public List<Review> getReviewList(int storeId, Search search) {

        System.out.println("getReviewList(storeId, search) : 특정 가게의 리뷰 목록 조회");

        return communityDao.getReviewListByStoreId(storeId, search);
    }


    // 특정 회원들의 리뷰 리스트 조회 - 내 리뷰 , 다른사람의 리뷰 , 친구들의 리뷰
    @Override
    public List<Review> getReviewList(List<String> userNameList, Search search) {

        System.out.println("getReviewList(userNameList, search) : 특정 회원들의 목록 조회");

        return communityDao.getReviewListByUserName(userNameList, search);
    }


    //리뷰 업데이트
    @Override
    public void updateReview(Review review) throws Exception {

        System.out.println("updateReview(review)");

        communityDao.updateReview(review);
    }


    //리뷰 삭제 -> 노출여부 T => F 로 변경
    @Override
    public void removeReview(Review review) throws Exception {

        System.out.println("removeReview(review)");

        communityDao.removeReview(review);
    }


    //댓글 작성
    @Override
    public void addComment(Comment comment) throws Exception {

        System.out.println("addComment 호출" + comment );

        communityDao.addComment(comment);
    }


    //댓글 불러오다
    @Override
    public List<Comment> getCommentList(int reviewNo) throws Exception {

        System.out.println("getCommentList(reviewNo)");

        return communityDao.getCommentList(reviewNo);
    }


    //댓글 수정
    @Override
    public void updateComment(Comment comment) throws Exception {

        System.out.println("updateComment(comment)");

        communityDao.updateComment(comment);
    }


    //댓글 삭제 -> 노출여부 T => F 로 변경
    @Override
    public void removeComment(Comment comment) throws Exception {

        System.out.println("removeComment(comment)");

        communityDao.removeComment(comment);
    }

    //댓글 존재 여부 확인 메서드
    @Override
    public Comment getComment (int commentNo) throws  Exception{
        return communityDao.getCommentById(commentNo);
    }

}
