package com.placeHere.server.service.pointShop.impl;

import com.placeHere.server.dao.pointShop.PointDao;
import com.placeHere.server.dao.pointShop.PurchaseDao;
import com.placeHere.server.domain.Product;
import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.pointShop.PointService;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.pointShop.PurchaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    @Qualifier("purchaseDao")
    private PurchaseDao purchaseDao;

    @Autowired
    @Qualifier("pointDao")
    private PointDao pointDao;

    @Autowired
    @Qualifier("pointServiceImpl")
    private PointService pointService;


    // Constructor
    public PurchaseServiceImpl() {
    }

    // 상품 구매 => 리스트 형식으로 바꿔야함
    @Override
    public void addPurchase(Purchase purchase) throws Exception{

//        String barcodeNo = purchaseDao.getNextBarcodeNumber();
//        purchase.setBarcodeNo(barcodeNo);
//        int tranPoint = purchaseDao.calcTranPoint(purchase.getUserName());
//        purchase.setTranPoint(tranPoint);

        purchaseDao.addPurchase(purchase);

//        Product product = new Product();
//        User user = new User();
//
//        String username = "user1";
//        int tranPoint = -20000;
//        String depType = "상품 구매";
//        int currPoint = 23300;
//        int relNo = 9;
//
//        pointService.addPointTransaction(username, tranPoint, depType, currPoint, relNo);
//
//        pointService.updatePoint(username, tranPoint,30073);

    }

    // 구매한 상품 상세 정보 조회
    @Override
    public Purchase getPurchase(int tranNo) throws Exception{

        return purchaseDao.getPurchase(tranNo);

    }

    // 구매한 상품 목록 조회 => 수정 필요
    @Override
    public List<Purchase> getPurchaseList(String userName) throws Exception{

        List<Purchase> list = purchaseDao.getPurchaseList(userName);

        return list;
    }

    public String getNextBarcodeNumber() throws Exception {
        return purchaseDao.getNextBarcodeNumber();
    }

    // 장바구니 추가
    @Override
    public void addCart(Purchase purchase) throws Exception {

        purchaseDao.addCart(purchase);
    }

    // 찜 목록 추가
    @Override
    public void addWish(Purchase purchase) throws Exception {

        purchaseDao.addWish(purchase);
    }

    // 장바구니 목록 조회
    @Override
    public List<Purchase> getCartList(String userName) throws Exception {

        return purchaseDao.getCartList(userName);
    }

    // 찜 목록 조회
    @Override
    public List<Purchase> getWishList(String userName) throws Exception {
        return purchaseDao.getWishList(userName);
    }

    // 찜 / 장바구니 삭제
    @Override
    public void removeWishCart(Purchase purchase) throws Exception {
        purchaseDao.removeWishCart(purchase);
    }

    // 찜 목록 삭제
//    @Override
//    public void removeWish(int wishCartNo) throws Exception {
//        purchaseDao.removeWish(wishCartNo);
//    }

    @Override
    public boolean isProductInWishList(int prodNo, String username) throws Exception {
        int count = purchaseDao.isProductInWishList(prodNo, username);
        return count > 0;
    }

    @Override
    public int getWishListCount(String userName) {

        return purchaseDao.getWishListCount(userName);
    }

    @Override
    public int getCartListCount(String userName) {

        return purchaseDao.getCartListCount(userName);
    }

}