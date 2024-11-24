package com.placeHere.server;

import com.placeHere.server.dao.pointShop.ProductDao;
import com.placeHere.server.domain.Product;
import com.placeHere.server.service.pointShop.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    //@Transactional
    //@Test
//    public void testAddProduct() throws Exception {
//        Product product = new Product();
//        product.setProdName("배달 상품권");
//        product.setProdDetail("ddd");
//        product.setProdPrice(111111);
//        product.setProdImg1("abcdefghijk.jpg");
//        product.setProdImg2("111abcdefghijk.jpg");
//        product.setProdImg3("123abcdefghijk.jpg");
//        product.setProdCateNo(1);
//        product.setProdCateName("배달 상품권");
//        product.setRegDt(Date.valueOf("2024-11-21"));
//        product.setProdStatus(true);
//
//        // 상품 추가 테스트
//        productService.addProduct(product);
////        Product fetchedProduct = productService.getProduct(product.getProdNo());
//
//        // 결과 확인
////        Assertions.assertNotNull(fetchedProduct);
////        Assertions.assertEquals("ddd", fetchedProduct.getProdName());
//        System.out.println("1234"+product);
//
//    }

//    @Transactional
    @Test
    public void testGetProduct() throws Exception {
        Product product = productService.getProduct(9);
//        Assertions.assertNotNull(product);
//        Assertions.assertEquals(1, product.getProdNo());
        System.out.println("1234" + product);
        System.out.println("5678");
    }

    //@Transactional
//    @Test
//    public void testUpdateProduct() throws Exception {
//        Product product = productService.getProduct(9);
//        product.setProdName("updatedName");
//        productService.updateProduct(product);
//
//        Product updatedProduct = productService.getProduct(9);
////        Assertions.assertEquals("updatedName", updatedProduct.getProdName());
//        System.out.println("updateProduct : "+updatedProduct);
//    }

    //@Transactional
//    //@Test
//    public void testGetProductListAll() throws Exception {
//        Search search = new Search();
//        //search.setCurrentPage(1);
//        //search.setPageSize(3);
//        Map<String, Object> map = productService.getProductList(search);
//
//        List<Object> list = (List<Object>) map.get("list");
//        Assertions.assertEquals(3, list.size());
//    }

}
