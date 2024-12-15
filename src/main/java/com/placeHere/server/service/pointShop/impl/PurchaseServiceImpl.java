package com.placeHere.server.service.pointShop.impl;

import com.placeHere.server.dao.pointShop.PointDao;
import com.placeHere.server.dao.pointShop.PurchaseDao;
import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.PointService;
import com.placeHere.server.service.pointShop.PurchaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

//        List<Purchase> cartItems = purchaseDao.getCartList(purchase.getUsername());
//
//        for (Purchase purchase : cartItems) { // 구매 일자 설정
//            purchaseMapper.insertPurchase(purchase);  // 구매 기록 저장
//        }
//        // 구매 후 장바구니 비우기
//        purchaseMapper.clearWishCartByUserId(userId);  // 장바구니 비우기

        purchaseDao.addPurchase(purchase);

    }

    // 구매한 상품 상세 정보 조회
    @Override
    public Purchase getPurchase(int tranNo) throws Exception{

        return purchaseDao.getPurchase(tranNo);

    }

    // 구매한 상품 목록 조회 => 수정 필요
    @Override
    public List<Purchase> getPurchaseList(Search search) throws Exception{

        List<Purchase> list = purchaseDao.getPurchaseList(search);

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
    public List<Purchase> getCartList(String username) throws Exception {

        return purchaseDao.getCartList(username);
    }

    // 찜 목록 조회
    @Override
    public List<Purchase> getWishList(String username) throws Exception {
        return purchaseDao.getWishList(username);
    }

    // 찜 / 장바구니 삭제
    @Override
    public void removeWish(Purchase purchase) throws Exception {
        purchaseDao.removeWish(purchase);
    }

    public void removeCart(Purchase purchase) throws Exception{
        purchaseDao.removeCart(purchase);
    }

    // 찜 목록 삭제
//    @Override
//    public void removeWish(int wishCartNo) throws Exception {
//        purchaseDao.removeWish(wishCartNo);
//    }

    @Override
    public int isProductInWishList(Purchase purchase) throws Exception {
        int count = purchaseDao.isProductInWishList(purchase);
        return count;
    }

    @Override
    public int getWishListCount(String username) {

        return purchaseDao.getWishListCount(username);
    }

    @Override
    public int getCartListCount(String username) {

        return purchaseDao.getCartListCount(username);
    }

    // 선택된 상품들 일괄 구매 처리
    public void buySelectedItems(List<Purchase> selectedItems) throws Exception {
        purchaseDao.buySelectedItems(selectedItems);
    }

    // 선택된 상품 삭제 처리
    public void removeSelectedItems(List<Purchase> selectedItems) throws Exception {
        for (Purchase purchase : selectedItems) {
            purchaseDao.removeCart(purchase);
        }
    }

    @Transactional
    @Override
    public void purchaseProducts(String username) throws Exception {
        // 장바구니 상품 조회
        List<Purchase> cartItems = purchaseDao.getCartList(username);

//        for (Purchase purchase : cartItems) { // 구매 일자 설정
//            purchaseDao.addPurchase(purchase);  // 구매 기록 저장
//        }
        // 구매 후 장바구니 비우기
        purchaseDao.clearWishCartByUsername(username);  // 장바구니 비우기
    }

}