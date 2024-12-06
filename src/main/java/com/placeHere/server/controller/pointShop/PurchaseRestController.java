package com.placeHere.server.controller.pointShop;


import com.placeHere.server.domain.Purchase;
import com.placeHere.server.service.pointShop.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-purchase")
public class PurchaseRestController {

    @Autowired
    private PurchaseService purchaseService;

    // 장바구니에 제품 추가
    @PostMapping("/addCart")
    public String addCart(@RequestBody Purchase purchase) {
        try {
            String username = "user1";
            System.out.println("username : " + username);
            purchase.setUsername(username);
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
            String username = "user1";
            System.out.println("username : " + username);
            purchase.setUsername(username);
            boolean isWishExist = purchaseService.isProductInWishList(purchase.getProdNo(), username);
            System.out.println("prodNo : "+ purchase.getProdNo());

            if (isWishExist) {
                purchaseService.removeWish(purchase);
//                purchaseService.removeWishCart(10028);
                return "찜 목록에 상품이 삭제되었습니다.";
            }

            int wishListCount = purchaseService.getWishListCount(username);
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
    @DeleteMapping("/removeWish")
    public String removeWish(@ModelAttribute("purchase") Purchase purchase) {

        System.out.println("removeWish");

        try {
             purchaseService.removeWish(purchase);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "찜 목록 삭제 실패: " + e.getMessage();
        }
    }

    @DeleteMapping("/removeCart")
    public String removeCart(@ModelAttribute("purchase") Purchase purchase) {

        System.out.println("removeCart");

        try {
            purchaseService.removeCart(purchase);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "찜 목록 삭제 실패: " + e.getMessage();
        }
    }
}
