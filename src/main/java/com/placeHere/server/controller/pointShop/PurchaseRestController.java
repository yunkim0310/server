package com.placeHere.server.controller.pointShop;


import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.User;
import com.placeHere.server.service.pointShop.PurchaseService;
import jakarta.servlet.http.HttpSession;
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
    public String addCart(HttpSession session,
                          @RequestBody Purchase purchase,
                          Model model) {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
        try {
            String username = user.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(user);
            System.out.println("Buyer : " + user);
//            int prodNo = purchase.getProdNo();
            System.out.println("prodNo : "+ purchase.getProdNo());
            int isCartExist = purchaseService.isProductInCartList(purchase);

            if (isCartExist != 0) {
                return "이미 추가된 상품입니다.";
            }

            int wishCartCount = purchaseService.getCartListCount(username);
            if(5 <= wishCartCount){
                return "장바구니가 꽉찼습니다.";
            }else {
                purchaseService.addCart(purchase);
                return "장바구니에 상품이 추가되었습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 추가 실패: " + e.getMessage();
        }
        }else{
            return "redirect:/";
        }
    }

    // 찜 목록에 제품 추가
    @PostMapping("/addWish")
    public String addWish(HttpSession session,
                            @RequestBody Purchase purchase,
                          Model model) {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
        try {
            String username = user.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(user);
            System.out.println("Buyer : " + user);
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
        }else{
            return "redirect:/";
        }
    }

    // 찜 목록에서 제품 삭제
    @DeleteMapping("/removeWish")
    public String removeWish(@ModelAttribute("purchase") Purchase purchase, HttpSession session,
                             Model model) {

        System.out.println("removeWish");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
        try {
            String username = user.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(user);
            System.out.println("Buyer : " + user);
             purchaseService.removeWish(purchase);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "찜 목록 삭제 실패: " + e.getMessage();
        }
        }else{
            return "redirect:/";
        }
    }

    @DeleteMapping("/removeCart")
    public String removeCart(@ModelAttribute("purchase") Purchase purchase,
                             HttpSession session, Model model) {

        System.out.println("removeCart");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
        try {
            String username = user.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(user);
            System.out.println("Buyer : " + user);
            purchaseService.removeCart(purchase);
            model.addAttribute("username", username);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 삭제 실패: " + e.getMessage();
        }
        }else{
            return "redirect:/";
        }
    }

    @DeleteMapping("/clearCart")
    public String clearCart(@ModelAttribute("purchase") Purchase purchase,
                            HttpSession session, Model model) {

        System.out.println("removeCart");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
        try {
            String username = user.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(user);
            System.out.println("Buyer : " + user);
            purchaseService.clearWishCartByUsername(username);
            model.addAttribute("username", username);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 삭제 실패: " + e.getMessage();
        }
        }else{
            return "redirect:/";
        }
    }

    @DeleteMapping("/clearWish")
    public String clearWish(@ModelAttribute("purchase") Purchase purchase,
                            HttpSession session, Model model) {

        System.out.println("clearWish");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
        try {
            String username = user.getUsername();
            System.out.println("username : " + username);
            purchase.setBuyer(user);
            System.out.println("Buyer : " + user);
            purchaseService.clearWishByUsername(username);
            model.addAttribute("username", username);

//            purchaseService.removeWishCart(wishCartNo);
            return "상품이 삭제되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "장바구니 삭제 실패: " + e.getMessage();
        }
        }else{
            return "redirect:/";
        }
    }

}
