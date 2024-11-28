package com.placeHere.server.service.store.impl;

import com.placeHere.server.service.store.SearchService;

import java.util.List;

public class SearchServiceImpl implements SearchService {
    
    // 검색 내역 추가
    @Override
    public void addSearch(List<String> searchKeywordList) {
        
    }
    
    // 인기 검색어 조회
    @Override
    public List<String> getPopularKeyword() {
        return List.of();
    }

}
