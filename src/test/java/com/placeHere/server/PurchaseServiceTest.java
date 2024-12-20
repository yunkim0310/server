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

    @Transactional
    @Test
    public void testAddPurchase() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setUserName("user1");
        purchase.setPurchaseProd(productService.getProduct(9));
        purchase.setBarcodeNo("1234567890009");
        purchase.setBarcodeName("1234567890009");
        //purchase.setTranPoint(1000);
        purchase.setCntProd(2);
        purchase.setPointDt(Date.valueOf("2024-11-25"));
        purchase.setCurrPoint(10000);

        purchaseService.addPurchase(purchase);

        Assertions.assertEquals("user1", purchase.getUserName());
        Assertions.assertEquals("1234567890009", purchase.getBarcodeNo());
        Assertions.assertEquals("1234567890009", purchase.getBarcodeName());
        Assertions.assertEquals(2, purchase.getCntProd());
        Assertions.assertEquals(Date.valueOf("2024-11-25"), purchase.getPointDt());
        Assertions.assertEquals(10000, purchase.getCurrPoint());

        System.out.println("addPurchase : "+purchase);

    }

    @Test
    public void testGetPurchase() throws Exception {
        Purchase purchase = purchaseService.getPurchase(20001);

        Assertions.assertEquals(20001, purchase.getTranNo());
        Assertions.assertEquals("user1", purchase.getUserName());
        Assertions.assertNotNull(purchase.getPurchaseProd());
        Assertions.assertEquals(1, purchase.getPurchaseProd().getProdNo());
        Assertions.assertEquals("배달 상품권", purchase.getPurchaseProd().getProdName());
        Assertions.assertEquals(10000, purchase.getPurchaseProd().getProdPrice());
        Assertions.assertEquals("1234567890001", purchase.getBarcodeNo());
        Assertions.assertEquals(5000, purchase.getTranPoint());
        Assertions.assertEquals(1, purchase.getCntProd());
        Assertions.assertEquals(Date.valueOf("2024-11-21"), purchase.getPointDt());
        Assertions.assertEquals(15000, purchase.getCurrPoint());
        Assertions.assertEquals("상품 구매", purchase.getDepType());
        Assertions.assertEquals(0, purchase.getWishCartNo());
        Assertions.assertEquals("delivery_giftcard.jpg", purchase.getPurchaseProd().getProdImg1());
        Assertions.assertNull(purchase.getPurchaseProd().getProdImg2());
        Assertions.assertNull(purchase.getPurchaseProd().getProdImg3());

        System.out.println("getPurchase : " + purchase);
    }

//    @Test
    public void testGetPurchasesList() throws Exception {

        String userName = "user1";

        Map<String, Object> purchaseList = purchaseService.getPurchaseList(userName);



        System.out.println("getPurchaseList : " + purchaseList);
    }

//  @Test
    public void testAddCart() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setUserName("user1");
        purchase.setProdNo(1);
        purchase.setCntProd(2);

        purchaseService.addCart(purchase);

        Assertions.assertEquals("user1", purchase.getUserName());
        Assertions.assertEquals(1, purchase.getProdNo());
        Assertions.assertEquals(2, purchase.getCntProd());

        System.out.println("Purchase : " + purchase);

    }

    @Test
    public void testGetCartList() throws Exception {

        List<Purchase> cartList = purchaseService.getCartList("user1");

        System.out.println("user1's CartList : " + cartList);
    }

//    @Transactional
//    @Test
    public void testRemoveCart() throws Exception {
        int wishCartNo = 10003;

        purchaseService.removeCart(wishCartNo);

        System.out.println("Remove cart : " + wishCartNo);
    }

    @Test
    public void testAddWish() throws Exception {

        Purchase purchase = new Purchase();
        purchase.setUserName("user1");
        purchase.setProdNo(3);
        purchase.setCntProd(0);

        purchaseService.addWish(purchase);

        Assertions.assertEquals("user1", purchase.getUserName());
        Assertions.assertEquals(3, purchase.getProdNo());
        Assertions.assertEquals(0, purchase.getCntProd());

        System.out.println("AddWish : " + purchase);
    }

    @Test
    public void testGetWishList() throws Exception {

        List<Purchase> wishList = purchaseService.getWishList("user1");

        System.out.println("WishList : " + wishList);
    }

//    @Test
    public void testRemoveWish() throws Exception {
        int wishCartNo = 10005;

        purchaseService.removeWish(wishCartNo);

        System.out.println("Remove Wish : " + wishCartNo);

    }

}
