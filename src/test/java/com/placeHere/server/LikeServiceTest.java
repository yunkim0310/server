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
//    @Test
    public void testAddLike() throws Exception {
        Like like = new Like();

        likeService.addLike("test마무리", 3, "store");

        System.out.println(like);
    }

    // 좋아요 취소
//    @Test
    public void testRemoveLike() throws Exception {

        Like like = new Like();
        like.setLikeId(19);

        likeService.removeLike(like);
    }

// 좋아요 토탈 카운트
    @Test
    public void testTotalCount() throws  Exception {

        likeService.totalCount(2);

        System.out.println("totalCount :: ");
    }

    //좋아요 리스트 -> 테스트 실패 (받아오는 값 다 null)
//    @Test
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

        likeService.checkLike(like);
        System.out.println("testCheck ::" + like);

    }



    }
