package com.placeHere.server.service.pointShop;

import java.util.List;
import java.util.Map;

import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.Product;

public interface ProductService {

    // Method
    // 상품 등록
    public void addProduct(Product product) throws Exception;

    // 상품 정보 조회
    public Product getProduct(int prodNo) throws Exception;

    // 상품 목록 조회
    public Map<String, Object> getProductList(Search search) throws Exception;

    // 상품 수정
    public void updateProduct(Product product) throws Exception;

//    // 자동 입력
//    public List<String> getAutocomplete(String prodName) throws Exception;

//    // 상품 검색
//    public void searchProduct(Search search) throws Exception;
//
//    // 상품 카테고리 조회
//    public Product getProductCategory(String prodCateName) throws Exception;





}
