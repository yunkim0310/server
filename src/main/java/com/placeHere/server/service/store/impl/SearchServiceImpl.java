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
    public boolean addSearch(String searchKeyword) {

        try {
            searchDao.addSearch(searchKeyword.split(" "));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getPopularKeyword() {

        List<String> popularKeywordList = searchDao.getPopularKeywordList();

        if (popularKeywordList.size() < 10) {

            int popularKeywordCnt = popularKeywordList.size();

            for (int i = 0; i < 10 - popularKeywordCnt; i++) {
                popularKeywordList.add("");
            }
        }

        return popularKeywordList;
    }
}
