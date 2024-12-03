package com.placeHere.server;

import com.placeHere.server.domain.Comment;
import com.placeHere.server.domain.Review;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.reservation.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

@SpringBootTest
public class CommuniryServiceTest {

    @Autowired
    @Qualifier("communityServiceImpl")
    private CommunityService communityService;

    @Autowired
    @Qualifier("reservationServiceImpl")
    private ReservationService reservationService;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    @Value("${comment_size}")
    private int commentSize;




    // 특정 가게 리뷰 목록 조회
    @Test
    public void getReviewList() throws Exception {

        Search search = new Search(pageSize, listSize);
//        search.setCommentSize(commentSize);


        List<Review> reviewListALL = communityService.getReviewList(search);
        List<Review> reviewListByStoreId = communityService.getReviewList(1, search);
        List<Review> reviewListByUserName = communityService.getReviewList(List.of("Alice Kim"), search);
        List<Comment> getCommentList = communityService.getCommentList(42);

        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println();
        System.out.println();

//        System.out.println(search);
//        System.out.println(reviewListALL);
        System.out.println(reviewListByStoreId);
//        System.out.println(reviewListByUserName);
//        System.out.println(communityService.getCommentList(1));

        System.out.println();
        System.out.println();
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
    }

    @Test
    public void getCommentList() throws Exception{
        int reviewNO = 42;
        List<Comment> commentList  = communityService.getCommentList(reviewNO);

        System.out.println("test :: "  + commentList);
    }


    // 리뷰 작성
    @Test
    public void addReview() throws Exception {

        Review review = new Review();
        review.setRsrvNo(1);
        review.setReviewScore(5);
        review.setReviewContent("맛이 진하고 좋아요");
        review.setUserName("user01");

        communityService.addReview(review);

    }

    @Test
    public void testGetReview() throws Exception{
        int reviewNo = 5;

        Review review = communityService.getReview(reviewNo);

        System.out.println("111111111111===============");

        System.out.println("tets :: " + communityService.getReview(5));

        System.out.println("111111111111===============");
//        Review review = new Review();
//        review.setReviewNo(1);
//
//        communityService.getReview(1);
    }


    @Test
    public void testAddComment() throws  Exception{

        Comment comment = new Comment();

        comment.setReviewNo(44);
        comment.setUserName("testUser");
        comment.setCommentsContent("test중");

        communityService.addComment(comment);
    }
}
