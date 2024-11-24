package com.placeHere.server.service.pointShop.impl;

import com.placeHere.server.dao.pointShop.PurchaseDao;
import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.pointShop.PurchaseService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    // Constructor
    public PurchaseServiceImpl() {
    }

    public void addPurchase(Purchase purchase) throws Exception{

//        String barcodeNo = purchaseDao.getNextBarcodeNumber();
//        purchase.setBarcodeNo(barcodeNo);
        int tranPoint = purchaseDao.calcTranPoint(purchase.getUserName());
        purchase.setTranPoint(tranPoint);
        purchaseDao.addPurchase(purchase);

    }

    public Purchase getPurchase(int tranNo) throws Exception{

        return purchaseDao.getPurchase(tranNo);

    }

    //수정 필요
    public Map<String, Object> getPurchaseList(String userName) throws Exception{

//        List<Purchase> list = purchaseDao.getPurchaseList(search, userName);

        Map<String, Object> map = new HashMap<String , Object>();
        map.put("list", purchaseDao.getPurchaseList(userName));

        return map;
    }

//    public String getNextBarcodeNumber() throws Exception {
//        return purchaseDao.getNextBarcodeNumber();
//    }

    @Override
    public void addCart(Purchase purchase) throws Exception {
        purchaseDao.addCart(purchase); // 장바구니에 추가
    }

    @Override
    public void addWish(Purchase purchase) throws Exception {
        purchaseDao.addWish(purchase); // 찜 목록에 추가
    }

    @Override
    public List<Purchase> getCartList(String userName) throws Exception {
        return purchaseDao.getCartList(userName); // 사용자 장바구니 목록 조회
    }

    @Override
    public List<Purchase> getWishList(String userName) throws Exception {
        return purchaseDao.getWishList(userName); // 사용자 찜 목록 조회
    }

    @Override
    public void removeCart(int wishCartNo) throws Exception {
        purchaseDao.removeCart(wishCartNo); // 장바구니에서 항목 삭제
    }

    @Override
    public void removeWish(int wishCartNo) throws Exception {
        purchaseDao.removeWish(wishCartNo); // 찜 목록에서 항목 삭제
    }

}
