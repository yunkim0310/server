package com.placeHere.server.service.store;

import java.util.List;

public interface SearchService {

    // Method
    // 검색어 등록
    public void addSearch(String searchKeyword);

    // 인기 검색어 조회
    public List<String> getPopularKeyword();

}
