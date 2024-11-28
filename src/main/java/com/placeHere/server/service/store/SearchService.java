package com.placeHere.server.service.store;

import java.util.List;

public interface SearchService {

    // 검색 내역 추가
    public void addSearch(List<String> searchKeywordList);

    // 인기 검색어 조회
    public List<String> getPopularKeyword();

}
