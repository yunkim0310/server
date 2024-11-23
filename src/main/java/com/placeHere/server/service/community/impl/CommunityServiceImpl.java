package com.placeHere.server.service.community.impl;

import com.placeHere.server.dao.community.CommunityDao;
import com.placeHere.server.domain.Community;
import com.placeHere.server.service.community.CommunityService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Service("communityServiceImpl")
public class CommunityServiceImpl implements CommunityService {

    //Field
    @Autowired
    @Qualifier("communityDao")
    private CommunityDao communityDao;


  //Constructor
    public CommunityServiceImpl(){
    }

    //Method
    public void addReview(Community community) throws Exception{
        communityDao.addReview(community);
    }

    public Community getReview(int reviewNo) throws Exception{
        return communityDao.getReview(reviewNo);
    }

    @Override
    public List<Community> getReviewList() throws Exception {
        return communityDao.getReviewList();
    }

    public void updateReview(Community community) throws Exception{
        communityDao.updateReview(community);
    }

    public void removeReview(Community community) throws  Exception{
        communityDao.removeReview(community);
    }

    public void addComment(Community community) throws Exception{
        communityDao.addComment(community);
    }

    public Community getComment(int commentNo)throws Exception{
        return communityDao.getComment(commentNo);
    }

    public void updateComment(Community community) throws Exception{
        communityDao.updateComment(community);
    }

    public void removeComment(Community community) throws Exception{
        communityDao.removeComment(community);
    }

}
