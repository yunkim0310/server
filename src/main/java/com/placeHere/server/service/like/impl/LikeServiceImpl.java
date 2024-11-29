package com.placeHere.server.service.like.impl;

import com.placeHere.server.dao.like.LikeDao;
import com.placeHere.server.domain.Like;
import com.placeHere.server.service.like.LikeService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Setter
@Service("likeServiceImpl")
public class LikeServiceImpl implements LikeService {
//    Field
    @Autowired
    @Qualifier("likeDao")
     private LikeDao likeDao;
    //Method
    //좋아요 추가
    public void addLike(String userName, int relationNo, String target) throws Exception{
        likeDao.addLike(userName , relationNo, target);

    }

    // 좋아요 취소
    public boolean removeLike(Like like) throws Exception {
        likeDao.removeLike(like);
        return true;
    }

    // 좋아요 토탈 카운트
    public Long  totalCount(int relationNo) throws Exception{
        return likeDao.totalCount(relationNo);
    }

    //좋아요 리스트 ( 인기 가게 => 좋아요가 가장 많은 번호의 리스트 )
    public List<Integer> likeList(String target) throws Exception{
        return likeDao.likeList(target);
    }

//    // 좋아요 검증
    public Like checkLike(Like like) throws Exception{
        return likeDao.checkLike(like);
    }
    
    // 가게 좋아요 목록 조회
    @Override
    public List<Like> getStoreLikeList(String UserName) throws Exception {
        return likeDao.getStoreLikeList(UserName);
    }

}
