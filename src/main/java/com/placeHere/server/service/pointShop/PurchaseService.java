package com.placeHere.server.service.pointShop;

import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface PurchaseService {

    //상품 구매 => 리스트 형식으로 바꿔야함
    public void addPurchase(Purchase purchase) throws Exception;

    //구매 상품 조회
    public Purchase getPurchase(int tranNo) throws Exception;

    //구매 상품 목록
    public List<Purchase> getPurchaseList(Search search) throws Exception;

    //바코드 번호
    public String getNextBarcodeNumber() throws Exception;

    // 장바구니 추가
    public void addCart(Purchase purchase) throws Exception;

    // 찜 목록 추가
    public void addWish(Purchase purchase) throws Exception;

    // 장바구니 목록 조회
    public List<Purchase> getCartList(String username) throws Exception;

    // 찜 목록 조회
    public List<Purchase> getWishList(String username) throws Exception;

    // 찜 / 장바구니 삭제
    public void removeWish(Purchase purchase) throws Exception;

    // 찜 목록 삭제
    public void removeCart(Purchase purchase) throws Exception;

    public int isProductInWishList(Purchase purchase) throws Exception;

    public int getWishListCount(String username);

    public int getCartListCount(String username);

    public void buySelectedItems(List<Purchase> selectedItems) throws Exception;

    public void removeSelectedItems(List<Purchase> selectedItems) throws Exception;


    @Transactional
    public void purchaseProducts(String username) throws Exception;

    public void clearWishCartByUsername(String username) throws Exception;
}