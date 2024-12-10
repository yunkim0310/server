package com.placeHere.server.controller.pointShop;


import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.pointShop.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-purchase")
public class PurchaseRestController {

    @Autowired
    private PurchaseService purchaseService;

    // 장바구니에 제품 추가
    @PostMapping("/addCart")
    public String addCart(@SessionAttribute("user") User buyer,
                            @RequestBody Purchase purchase) {
        try {
            String username = buyer.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(buyer);
            purchaseService.addCart(purchase);
            return "장바구니에 상품이 추가되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 추가 실패: " + e.getMessage();
        }
    }

    // 찜 목록에 제품 추가
    @PostMapping("/addWish")
    public String addWish(@SessionAttribute("user") User buyer,
                            @RequestBody Purchase purchase) {
        try {
            String username = buyer.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(buyer);
            System.out.println("Buyer : " + buyer);
//            int prodNo = purchase.getProdNo();
            System.out.println("prodNo : "+ purchase.getProdNo());
            int isWishExist = purchaseService.isProductInWishList(purchase);
//            purchase.isSelected(isWishExist);

            if (isWishExist != 0) {
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
    public String removeWish(@ModelAttribute("purchase") Purchase purchase, @SessionAttribute("user") User buyer) {

        System.out.println("removeWish");

        try {
            String username = buyer.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(buyer);
            System.out.println("Buyer : " + buyer);
             purchaseService.removeWish(purchase);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "찜 목록 삭제 실패: " + e.getMessage();
        }
    }

    @DeleteMapping("/removeCart")
    public String removeCart(@ModelAttribute("purchase") Purchase purchase,
                             @SessionAttribute("user") User buyer, Model model) {

        System.out.println("removeCart");

        try {
            String username = buyer.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(buyer);
            System.out.println("Buyer : " + buyer);
            purchaseService.removeCart(purchase);
            model.addAttribute("username", username);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 삭제 실패: " + e.getMessage();
        }
    }
}
