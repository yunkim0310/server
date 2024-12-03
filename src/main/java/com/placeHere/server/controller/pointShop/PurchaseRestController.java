package com.placeHere.server.controller.pointShop;


import com.placeHere.server.domain.Purchase;
import com.placeHere.server.service.pointShop.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseRestController {

    @Autowired
    private PurchaseService purchaseService;

    // 장바구니에 제품 추가
    @PostMapping("/addCart")
    public String addCart(@RequestBody Purchase purchase) {
        try {
            String userName = "user1";
            System.out.println("userName : " + userName);
            purchase.setUserName(userName);
            purchaseService.addCart(purchase);
            return "장바구니에 상품이 추가되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 추가 실패: " + e.getMessage();
        }
    }

    // 찜 목록에 제품 추가
    @PostMapping("/addWish")
    public String addWish(@RequestBody Purchase purchase) {
        try {
            String userName = "user1";
            System.out.println("userName : " + userName);
            purchase.setUserName(userName);
            boolean isWishExist = purchaseService.isProductInWishList(purchase.getProdNo(), userName);
            System.out.println("prodNo : "+ purchase.getProdNo());

            if (isWishExist) {
                purchaseService.removeWishCart(purchase);
//                purchaseService.removeWishCart(10028);
                return "찜 목록에 상품이 삭제되었습니다.";
            }

            int wishListCount = purchaseService.getWishListCount(userName);
            if(5 <= wishListCount){
                return "찜 목록이 꽉찼습니다.";
            }else {
                purchaseService.addWish(purchase);
                purchase.setWishCartNo(purchase.getWishCartNo());
                return "찜 목록에 상품이 추가되었습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }

    // 찜 목록에서 제품 삭제
    @DeleteMapping("/removeWishCart")
    public String removeWishCart(@ModelAttribute("purchase") Purchase purchase) {
        try {
             purchaseService.removeWishCart(purchase);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "찜 목록 삭제 실패: " + e.getMessage();
        }
    }
}
