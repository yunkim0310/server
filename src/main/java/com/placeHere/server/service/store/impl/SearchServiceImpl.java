package com.placeHere.server.service.store.impl;

import com.placeHere.server.dao.store.SearchDao;
import com.placeHere.server.service.store.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    // Field
    @Autowired
    private SearchDao searchDao;


    @Override
    public void addSearch(String searchKeyword) {

        System.out.println(searchKeyword);
        searchDao.addSearch(searchKeyword.split(" "));
    }

    @Override
    public List<String> getPopularKeyword() {

        List<String> popularKeywordList = searchDao.getPopularKeywordList();
        System.out.println(popularKeywordList);

        return popularKeywordList;
    }
}
