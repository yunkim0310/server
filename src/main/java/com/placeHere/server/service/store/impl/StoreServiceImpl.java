package com.placeHere.server.service.store.impl;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.store.StoreService;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public class StoreServiceImpl implements StoreService {
    @Override
    public int chkDuplicateBusinessNo(String businessNo) {
        return 0;
    }

    @Override
    public int addStore(Store store) {
        return 0;
    }

    @Override
    public Store getStore(int storeId) {
        return null;
    }

    @Override
    public List<Store> getStoreList(Search search) {
        return List.of();
    }

    @Override
    public int updateStore(Store store) {
        return 0;
    }

    @Override
    public int removeStore(int storeId) {
        return 0;
    }

    @Override
    public int addOperation(StoreOperation storeOperation) {
        return 0;
    }

    @Override
    public StoreOperation getOperation(int storeId, Date effectDt) {
        return null;
    }

    @Override
    public StoreOperation getOperation(int storeId) {
        return null;
    }

    @Override
    public int addStoreNews(StoreNews storeNews) {
        return 0;
    }

    @Override
    public List<StoreNews> getStoreNewsList(int storeId) {
        return List.of();
    }

    @Override
    public int updateStoreNews(StoreNews storeNews) {
        return 0;
    }

    @Override
    public int removeStoreNews(int newsId) {
        return 0;
    }

    @Override
    public int addCloseday(Date closeday) {
        return 0;
    }

    @Override
    public List<Date> getClosedayList(int storeId) {
        return List.of();
    }

    @Override
    public int removeCloseday(int closedayId) {
        return 0;
    }

    @Override
    public Map<String, Place> getNearbyPlaces(String storeAddr) {
        return Map.of();
    }

    @Override
    public Map<String, Map<String, Integer>> getStatistics(int storeId) {
        return Map.of();
    }
}
