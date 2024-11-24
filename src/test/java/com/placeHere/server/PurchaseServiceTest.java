package com.placeHere.server;

import com.placeHere.server.domain.Product;
import com.placeHere.server.domain.Purchase;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.pointShop.PurchaseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class PurchaseServiceTest {

    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

//    @Transactional
//    @Test
//    public void testAddPurchase() throws Exception {
//        Purchase purchase = new Purchase();
//        purchase.setUserName("user1");
//        purchase.setPurchaseProd(productService.getProduct(1));
//        purchase.setBarcodeNo("1234567890008");
//        purchase.setBarcodeName("1234567890008");
//        //purchase.setTranPoint(1000);
//        purchase.setCntProd(1);
//        purchase.setTranDt(Date.valueOf("2024-11-24"));
//        purchase.setCurrPoint(5000);
//
//        // 상품 추가 테스트
//        purchaseService.addPurchase(purchase);
////        Product fetchedProduct = productService.getProduct(product.getProdNo());
//
//        // 결과 확인
////        Assertions.assertNotNull(fetchedProduct);
////        Assertions.assertEquals("ddd", fetchedProduct.getProdName());
//        System.out.println("1234"+purchase);
//
//    }

    //@Test
    public void testGetPurchase() throws Exception {
        Purchase purchase = purchaseService.getPurchase(20001);
//        Assertions.assertNotNull(product);
//        Assertions.assertEquals(1, product.getProdNo());
        System.out.println("Purchase : " + purchase);
    }

//    @Test
//    public void testGetPurchase() throws Exception {
//        // 테스트를 위한 더미 데이터 추가
//        Purchase newPurchase = new Purchase();
//        newPurchase.setTranNo(20001);
//        newPurchase.setUserName("user1");
//        newPurchase.setTranPoint(1000);
//        purchaseService.addPurchase(newPurchase);
//
//        // 테스트
//        Purchase purchase = purchaseService.getPurchase(20001);
////        Assertions.assertNotNull(purchase);
////        Assertions.assertEquals(20001, purchase.getTranNo());
//        System.out.println("Purchase: " + purchase);
//    }

//    @Test
//    public void testGetPurchasesList() throws Exception {
//        // 예시 userName (사용자 이름)
//        String userName = "user1";  // 실제로 존재하는 사용자 이름으로 바꾸세요.
//
//        // purchaseMapper의 getPurchaseList 메소드 호출
//        Map<String, Object> purchaseList = purchaseService.getPurchaseList(userName);
//
//        // 전체 구매 목록 출력
//        System.out.println("PurchaseList : " + purchaseList);
//    }

//@Test
//void testAddCart() throws Exception {
//    Purchase purchase = new Purchase();
//    purchase.setUserName("user1");
//    purchase.setProdNo(1);
//    purchase.setCntProd(2);
//
//    purchaseService.addCart(purchase);
//
//    System.out.println("Purchase : " + purchase);
//
//}

//    @Test
//    void testGetCartList() throws Exception {
//        String userName = "user1";
//
//        purchaseService.getCartList(userName);
//
//        System.out.println("user1's CartList : " + purchaseService.getCartList(userName));
//    }
//
//    @Test
//    void testRemoveCart() throws Exception {
//        int wishCartNo = 10004;
//
//        purchaseService.removeCart(wishCartNo);
//
//        System.out.println("Remove cart : " + wishCartNo);
//    }

//@Test
//void testAddWish() throws Exception {
//    Purchase purchase = new Purchase();
//    purchase.setUserName("user1");
//    purchase.setProdNo(1);
//
//    purchaseService.addWish(purchase);
//
//    System.out.println("AddWish : " + purchase);
//}
//
//@Test
//void testGetWishList() throws Exception {
//    String userName = "user1";
//
//    // 테스트할 데이터를 준비합니다.
//    Purchase purchase = new Purchase();
//    purchase.setUserName(userName);
//    purchase.setProdNo(1);
//
//    System.out.println("WishList : " + purchase);
//}
//
    // 찜 목록 항목을 삭제하는 테스트
    @Test
    void testRemoveWish() throws Exception {
        int wishCartNo = 10005;

        purchaseService.removeWish(wishCartNo);

        System.out.println("Remove Wish : " + wishCartNo);

    }

}
