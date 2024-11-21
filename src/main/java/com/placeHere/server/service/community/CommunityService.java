package com.placeHere.server.service.community;

import com.placeHere.server.domain.Community;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface CommunityService {

    //INSERT
    public void addReview(Community community) throws Exception;


    //SELECT ONE
    public Community getReview(int reviewNo) throws  Exception;


    //SELECT LIST
    public Map<String, Object> getReviewList(Community community) throws Exception;


    //UPDATE
    public void updateReview(Community community) throws Exception;


    //DELETE
    public void removeReview(Community community) throws Exception;
}