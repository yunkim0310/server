package com.placeHere.server.service.pointShop.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.Product;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.dao.pointShop.ProductDao;

@Setter
@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService{

    @Autowired
    @Qualifier("productDao")
    private ProductDao productDao;

    public ProductServiceImpl() {

        System.out.println(this.getClass());

    }

    // 상품 등록
    public void addProduct(Product product) throws Exception{

        productDao.addProduct(product);

    }

    // 상품 정보 조회
    public Product getProduct(int prodNo) throws Exception{

        return productDao.getProduct(prodNo);

    }

    // 상품 목록 조회
    public Map<String,Object> getProductList(Search search) throws Exception {

        List<Product> list = productDao.getProductList(search);

        Map<String, Object> map = new HashMap<String , Object>();
        map.put("list", list);


        return map;

    }

    // 상품 수정
    public void updateProduct(Product prod) throws Exception{

        productDao.updateProduct(prod);

    }

    public List<String> getAutocomplete(String prodName) throws Exception{
        List<String> list = productDao.getAutocomplete("%"+prodName+"%");

        return list;
    }

//    // 상품 검색
//    public void searchProduct(Search search) throws Exception{
//
//        productDao.searchProduct(search);
//
//    }
//
//    // 상품 카테고리 조회
//    public Product getProductCategory(String prodCateName) throws Exception{
//
//        return productDao.getProductCategory(prodCateName);
//
//    }

}