package com.placeHere.server.controller.pointShop;

import com.placeHere.server.domain.Product;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.ProductService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product/*")
public class ProductController {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    public ProductController(){

        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");

    }

    @Value("${store_upload_dir}")
    String path;

    @Autowired
    ServletContext servletContext;

    public String getCategoryNameByNo(int prodCateNo) {
        switch (prodCateNo) {
            case 1:
                return "배달 상품권";
            case 2:
                return "베이커리 교환권";
            case 3:
                return "카페 교환권";
            case 4:
                return "아이스크림 교환권";
            case 5:
                return "치킨 교환권";
            case 6:
                return "버거/피자 교환권";
            case 7:
                return "편의점 교환권";
            default:
                return "알 수 없는 카테고리";
        }
    }

//    @RequestMapping( value="addProduct", method= RequestMethod.GET)
    @GetMapping("/addProduct")
    public String addProduct(Model model) throws Exception{

        System.out.println("/product/addProduct : GET");

        Product product = new Product();
        model.addAttribute("product", product);

        return "/pointShop/product/addProduct";
    }

//    @RequestMapping(value = "addProductResult", method = RequestMethod.POST)
    @PostMapping("/addProduct")
    public String addProductResult(
//                            @RequestParam("file") MultipartFile file,
                             @ModelAttribute("product") Product product, Model model) throws Exception {

        System.out.println("/product/addProduct : POST");

        String prodCateName = getCategoryNameByNo(product.getProdCateNo());
        product.setProdCateName(prodCateName);

//        String fileName = file.getOriginalFilename();
//        String uploadPath = servletContext.getRealPath(path);
//        String saveFile = uploadPath + fileName;
//        System.out.println("Image upload path: " + uploadPath);
//
//        try {
//            file.transferTo(new File(saveFile));
//            product.setProdImg1(fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        productService.addProduct(product);

        return "/pointShop/product/addProductResult";
    }

//    @RequestMapping( value="getProduct", method=RequestMethod.GET)
    @GetMapping("/getProduct")
    public String getProduct(@RequestParam("prodNo") int prodNo ,
                             @RequestParam(value = "wishCartNo", required = false) Integer wishCartNo,
                             HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        System.out.println("/product/getProduct : GET");

        Product product = productService.getProduct(prodNo);

        String userName = "user1";
        model.addAttribute("userName", userName);

        Cookie[] cookies = request.getCookies();
        String history = "";

        if (cookies!=null && cookies.length > 0) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals("history")) {
                    history = cookie.getValue();
                }
            }

            history += "/" + String.valueOf(product.getProdNo());
            Cookie cookie = new Cookie("history", history);
            response.addCookie(cookie);

        }else{

            Cookie cookie = new Cookie("history", String.valueOf(product.getProdNo()));

            response.addCookie(cookie);
        }


        model.addAttribute("product" , product);
        if (wishCartNo != null) {
            model.addAttribute("wishCartNo", wishCartNo);
        }

        return "/pointShop/product/getProduct";

    }

//    @RequestMapping( value="updateProduct", method=RequestMethod.GET)
    @GetMapping("/updateProduct")
    public String updateProduct( @RequestParam("prodNo") int prodNo , Model model) throws Exception {

        System.out.println("/product/updateProduct : GET");

        Product product = productService.getProduct(prodNo);

        model.addAttribute("product" , product);

        return "/pointShop/product/updateProduct";
    }

//    @RequestMapping( value="updateProductResult", method=RequestMethod.POST)
    @PostMapping("/updateProduct")
    public String updateProductResult(
//                                        @RequestParam("file") MultipartFile file,
                                        @ModelAttribute("product") Product product ,
                                        Model model,
                                        HttpSession session) throws Exception {

        System.out.println("/product/updateProduct : POST");

        String prodCateName = getCategoryNameByNo(product.getProdCateNo());
        product.setProdCateName(prodCateName);

//        String fileName = file.getOriginalFilename();
//        String uploadPath = servletContext.getRealPath(path);
//        String saveFile = uploadPath + fileName;
//
//        try {
//            if(!file.isEmpty()) {
//                System.out.println("in");
//                file.transferTo(new File(saveFile));
//                product.setProdImg1(fileName);
//            }
//            productService.updateProduct(product);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        productService.updateProduct(product);

        model.addAttribute("product", product);

        System.out.println("Product: " + product);

        return "/pointShop/product/updateProductResult";
    }

    // @RequestParam("menu") String menu,
    @RequestMapping( value="listProduct")
    public String listProduct(@RequestParam(value = "order", required = false) String order,
                              @ModelAttribute("search") Search search ,
                              Model model) throws Exception {

        System.out.println("/product/listProduct : GET / POST");

        System.out.println("priceMin : " + search.getPriceMin());
        System.out.println("priceMax : " + search.getPriceMax());

        search.setOrder(order);
        System.out.println("order: " + order);

        if(search.getPage() == 0) {
            search.setPage(1);
        }
//        search.setPageSize(pageSize);

        List<Product> productList = productService.getProductList(search);

//        Search resultPage = new Search( search.getPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit , pageSize);
//        System.out.println(resultPage);

//        model.addAttribute("menu", menu);
        model.addAttribute("productList", productList);
//        model.addAttribute("resultPage" , resultPage);
        model.addAttribute("search" , search);

        return "/pointShop/product/listProduct";
    }

//    @RequestMapping( value="listProduct")
//    public String listProduct(
////            @RequestParam("prodNo") int prodNo,
//                              @RequestParam(value = "order", required = false) String order,
//                              @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
//                              Model model) throws Exception {
//
//        System.out.println("/product/listProduct : GET / POST");
//
//        Search search = new Search();
//
//        if (searchKeyword != null) {
//            search.setSearchKeyword(searchKeyword);
//        }
//        if (order != null) {
//            search.setOrder(order);
//        }
//
//        if(search.getPage() == 0) {
//            search.setPage(1);
//        }
//
//        List<Product> productList = productService.getProductList(search);
//
//        model.addAttribute("productList" , productList);
//
//        return "/pointShop/product/listProduct";
//    }




}