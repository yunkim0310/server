package com.placeHere.server.dao.store;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchDao {

    // Method
    // 검색어 등록
    public void addSearch(String[] searchKeywordArray);

    // 인기 검색어 조회
    public List<String> getPopularKeywordList();

}
