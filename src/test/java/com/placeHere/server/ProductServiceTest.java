package com.placeHere.server;

import com.placeHere.server.dao.pointShop.ProductDao;
import com.placeHere.server.domain.Product;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    @Transactional
    @Test
    public void testAddProduct() throws Exception {
        Product product = new Product();
        product.setProdName("배달 상품권");
        product.setProdDetail("ddd");
        product.setProdPrice(111111);
        product.setProdImg1("abcdefghijk.jpg");
        product.setProdImg2("111abcdefghijk.jpg");
        product.setProdImg3("123abcdefghijk.jpg");
        product.setProdCateNo(1);
        product.setProdCateName("배달 상품권");
        product.setRegDt(Date.valueOf("2024-11-21"));
        product.setProdStatus(true);


        productService.addProduct(product);
//        Product fetchedProduct = productService.getProduct(product.getProdNo());

        Assertions.assertEquals("배달 상품권", product.getProdName());
        Assertions.assertEquals("ddd", product.getProdDetail());
        Assertions.assertEquals(111111, product.getProdPrice());
        Assertions.assertEquals("abcdefghijk.jpg", product.getProdImg1());
        Assertions.assertEquals("111abcdefghijk.jpg", product.getProdImg2());
        Assertions.assertEquals("123abcdefghijk.jpg", product.getProdImg3());
        Assertions.assertEquals(1, product.getProdCateNo());
        Assertions.assertEquals("배달 상품권", product.getProdCateName());
        Assertions.assertEquals(Date.valueOf("2024-11-25"), product.getRegDt());

        System.out.println("1234"+product);

    }

//    @Transactional
    @Test
    public void testGetProduct() throws Exception {
        Product product = productService.getProduct(13);
//        Assertions.assertNotNull(product);
        Assertions.assertEquals(13, product.getProdNo());
        Assertions.assertEquals("배달 상품권", product.getProdName());
        Assertions.assertEquals("ddd", product.getProdDetail());
        Assertions.assertEquals(111111, product.getProdPrice());
        Assertions.assertEquals("abcdefghijk.jpg", product.getProdImg1());
        Assertions.assertEquals("111abcdefghijk.jpg", product.getProdImg2());
        Assertions.assertEquals("123abcdefghijk.jpg", product.getProdImg3());
        Assertions.assertEquals(1, product.getProdCateNo());
        Assertions.assertEquals("배달 상품권", product.getProdCateName());
        Assertions.assertEquals(Date.valueOf("2024-11-25"), product.getRegDt());

        System.out.println("getProduct : " + product);
//        System.out.println("5678");
    }

    @Transactional
    @Test
    public void testUpdateProduct() throws Exception {
        Product product = productService.getProduct(13);
        product.setProdName("updatedName");
        product.setProdStatus(false);
        productService.updateProduct(product);

        Product updatedProduct = productService.getProduct(13);

        Assertions.assertEquals(13, product.getProdNo());
        Assertions.assertEquals("updatedName", product.getProdName());
        Assertions.assertEquals("ddd", product.getProdDetail());
        Assertions.assertEquals(111111, product.getProdPrice());
        Assertions.assertEquals("abcdefghijk.jpg", product.getProdImg1());
        Assertions.assertEquals("111abcdefghijk.jpg", product.getProdImg2());
        Assertions.assertEquals("123abcdefghijk.jpg", product.getProdImg3());
        Assertions.assertEquals(1, product.getProdCateNo());
        Assertions.assertEquals("배달 상품권", product.getProdCateName());
        Assertions.assertEquals(Date.valueOf("2024-11-25"), product.getRegDt());


//        Assertions.assertEquals("updatedName", updatedProduct.getProdName());
        System.out.println("updateProduct : "+updatedProduct);
    }

    //@Transactional
    @Test
    public void testGetProductList() throws Exception {

        Search search = new Search();
        search.setSearchKeyword("배달");

        Map<String, Object> map = productService.getProductList(search);

        List<Object> list = (List<Object>) map.get("list");

        System.out.println("List : "+list);
    }

}
