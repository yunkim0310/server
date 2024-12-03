package com.placeHere.server;

import com.placeHere.server.dao.like.LikeDao;
import com.placeHere.server.domain.Like;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.like.LikeService;
import com.placeHere.server.service.like.impl.LikeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.Transient;
import java.sql.Date;
import java.util.List;

@SpringBootTest
public class LikeServiceTest {
    @Autowired
    @Qualifier("likeServiceImpl")
    private LikeService likeService;
    @Autowired
    private LikeDao likeDao;

    //좋아요 추가
    @Test
    public void testAddLike() throws Exception {
        Like like = new Like();

        likeService.addLike("user04", 1, "store");

        System.out.println(like);
    }

    // 좋아요 취소
    @Test
    public void testRemoveLike() throws Exception {

        Like like = new Like();
        like.setLikeId(21);

        likeService.removeLike(like);
    }

// 좋아요 토탈 카운트 ==> mapper에서 teart 0 번째만 가져옴 // if문 써서 변경 해보기
    @Test
    public void testTotalCount() throws  Exception {

        int relationNoStore = 0;  // 'store' 에 대해 null과 같은 상태
        String targetStore = "store";
        Long totalCountStore = likeService.getTotalCount(relationNoStore, targetStore);
        System.out.println("좋아요 토탈 카운트 (스토어) :: " + totalCountStore + "--- 타겟은 :: " + targetStore);

        int relationNoReview = 2;  // 'review'에 대해 2
        String targetReview = "review";
        Long totalCountReview = likeService.getTotalCount(relationNoReview, targetReview);
        System.out.println("좋아요 토탈 카운트 (리뷰) :: " + totalCountReview + "--- 타겟은 :: " + targetReview);

        int relationNoComment = 0;  // 'comment'에 대해 null 같은 상태
        String targetComment = "comment";
        Long totalCountComment = likeService.getTotalCount(relationNoComment, targetComment);
        System.out.println("좋아요 토탈 카운트 (댓글) :: " + totalCountComment + "--- 타겟은 :: " + targetComment);
    }
    //좋아요 리스트
    @Test
    public void testLikeList() throws Exception{

        Like like = new Like();
        like.setTarget("review");
        List<Integer> likeList = likeService.likeList("review");

        System.out.println("LikeList ::: " + likeList);

    }

    //좋아요 검증
    @Test
    public void testCheckLike() throws Exception{

        Like like = new Like();
        like.setUserName("testUser2");
        like.setRelationNo(1);
        like.setTarget("review");

        likeService.chkLike(like);
        System.out.println("testCheck ::" + like);

    }



    }
