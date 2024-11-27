package com.placeHere.server.service.pointShop;

import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;

import java.util.List;
import java.util.Map;

public interface PurchaseService {

    //상품 구매 => 리스트 형식으로 바꿔야함
    public void addPurchase(Purchase purchase) throws Exception;

    //구매 상품 조회
    public Purchase getPurchase(int tranNo) throws Exception;

    //구매 상품 목록
    public Map<String, Object> getPurchaseList(String userName) throws Exception;

    //바코드 번호
//    public String getNextBarcodeNumber() throws Exception;

    // 장바구니 추가
    public void addCart(Purchase purchase) throws Exception;

    // 찜 목록 추가
    public void addWish(Purchase purchase) throws Exception;

    // 장바구니 목록 조회
    public List<Purchase> getCartList(String userName) throws Exception;

    // 찜 목록 조회
    public List<Purchase> getWishList(String userName) throws Exception;

    // 찜 / 장바구니 삭제
    public void removeWishCart(int wishCartNo) throws Exception;

    // 찜 목록 삭제
//    public void removeWish(int wishCartNo) throws Exception;


}