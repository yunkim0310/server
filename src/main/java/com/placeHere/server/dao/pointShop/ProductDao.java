package com.placeHere.server.dao.pointShop;

import com.placeHere.server.domain.Product;
import com.placeHere.server.domain.Search;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductDao {

    public void addProduct(Product product) throws Exception;

    public Product getProduct(int prodNo) throws Exception;

    public List<Product> getProductList(Search search) throws Exception;

    public void updateProduct(Product product) throws Exception;

    public List<String> getAutocomplete(String prodName) throws Exception;

//    // 상품 검색
//    public void searchProduct(Search search) throws Exception;
//
//    // 상품 카테고리 조회
//    public Product getProductCategory(String prodCateName) throws Exception;
}