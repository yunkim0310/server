package com.placeHere.server.service.community.impl;

import com.placeHere.server.dao.community.CommunityDao;
import com.placeHere.server.domain.Community;
import com.placeHere.server.service.community.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("communityServiceImpl")
    public class CommunityServiceImpl implements CommunityService {

    //Field
    @Autowired
    private CommunityDao communityDao;
    public void setCommunityDao(CommunityDao communityDao){
        this.communityDao = communityDao;
  }

  //Constructor
    public CommunityServiceImpl(){
        System.out.println(this.getClass());
    }

    //Method
    public void addReview(Community community) throws Exception{
        communityDao.addReview(community);
    }

    public Community getReview(int reviewNo) throws Exception{
        return communityDao.getReview(reviewNo);
    }

   public Map<String,Object> getReviewList(int ReviewNo) throws Exception{
        List<Community> list = communityDao.getReviewList(community);


        Map<String, Object>map = new HashMap<String,Object>();
        map.put("list", list);

        return map;
   }

    public void updateReview(Community community) throws Exception{
        communityDao.updateReview(community);
    }

    public void removeReview(Community community) throws  Exception{
        communityDao.removeReview(community);
    }

}
