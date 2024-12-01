package com.placeHere.server.dao.store;

import java.util.List;

public interface SearchDao {

    // 검색 내역 추가
    public void addSearch(List<String> searchKeywordList);

    // 인기 검색어 조회
    public List<String> getPopularKeyword();

}
