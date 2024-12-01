package com.placeHere.server;

import com.placeHere.server.domain.Community;
import com.placeHere.server.domain.Reservation;
import com.placeHere.server.service.community.CommunityService;
import com.placeHere.server.service.reservation.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

//    @Test
    public void testAddReview() throws Exception {

        Community community = new Community();

        community.setRsrvNo(3);
        community.setReviewScore(5);
        community.setReviewImg1("test11111");
        community.setReviewImg2("제발112111");
        community.setReviewImg3("제발1241111");
        community.setReviewContent("testt111111111");
        community.setUserName("user01");

        communityService.addReview(community);



        System.out.println("community : " + community);


    }

//        @Test
    public void testGetReview() throws Exception {
        List<Community> communityList = communityService.getReviewList();

            System.out.println("test :::===== " + communityList);

    }

//    @Test
    public void testUpdateReview() throws Exception {

        Community community = communityService.getReview(10);
        Assertions.assertNotNull(community);

        System.out.println(community);

        community.setReviewScore(5);
        community.setReviewContent("test마무리");

        communityService.updateReview(community);

        Community updatedCommunity = communityService.getReview(10);

        System.out.println(community);

    }

//        @Test
    public void testRemoveReview() throws Exception {
        Community community = communityService.getReview(9);
        Assertions.assertNotNull(community);

        System.out.println(community);

        community.setReviewNo(9);

        communityService.removeReview(community);



        System.out.println("테스트 중 :::  " + community);

    }

//    @Test
    public void testAddComment() throws Exception {

        int reviewNo = 3;

        Community community = new Community();
        community.setReviewNo(2);
        community.setUserName("user05");
        community.setCommentContent("ㅎㅇ");
        community.setCommentDt(Date.valueOf("2024-11-25"));

        communityService.addComment(community);

    }

//        @Test
    public void testGetComment() throws Exception {
        Community community = communityService.getComment(3);

        System.out.println("testGetComment : " + community);

    }

//    @Test
   public void testUpdateComment() throws Exception{
        Community community = communityService.getComment(11);
        System.out.println("원래 댓글 정보 " + community);
        System.out.println("원래 댓글 내용" + community.getCommentContent());
        System.out.println("원래 댓글 번호" + community.getCommentNo());

        community.setCommentContent("댓글 내용 수정 Test");

        communityService.updateComment(community);

        Community updateCommunity = communityService.getComment(11);
        System.out.println(" 수정 댓글: " + updateCommunity);
        System.out.println(" 수정 댓글 내용 : " + updateCommunity.getCommentContent() );
    }

    @Test
    public void testRemoveComment() throws Exception{
        Community community = communityService.getComment(11);
        Assertions.assertNotNull(community);

//

        community.setCommentViewStatus(false);

        communityService.removeComment(community);


        Assertions.assertEquals(false, community.isCommentViewStatus());
    }


}
