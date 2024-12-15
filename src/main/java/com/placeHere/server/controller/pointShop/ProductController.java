package com.placeHere.server.controller.pointShop;

import com.placeHere.server.domain.*;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.pointShop.PurchaseService;
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
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product/*")
public class ProductController {

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;

    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;


    public ProductController(){

        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");

    }

    @Value("${pointShop_upload_dir}")
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
    public String addProduct(HttpSession session, Model model) throws Exception{

        System.out.println("/product/addProduct : GET");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_POINT")) {

            Product product = new Product();
            model.addAttribute("product", product);

            return "/pointShop/product/addProduct";
        }else{

            return "redirect:/";

        }
    }


//    @RequestMapping(value = "addProductResult", method = RequestMethod.POST)
    @PostMapping("/addProduct")
    public String addProductResult(
//                            @RequestParam(value = "file", required = false) MultipartFile file,
                             @ModelAttribute("product") Product product, Model model) throws Exception {

        System.out.println("/product/addProduct : POST");

        String prodCateName = getCategoryNameByNo(product.getProdCateNo());
        product.setProdCateName(prodCateName);
        product.setCntProd(1);

//        model.addAttribute("file", file);
//
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

        model.addAttribute("url", bucketUrl);

        return "/pointShop/product/addProductResult";
    }

//    @RequestMapping( value="getProduct", method=RequestMethod.GET)
    @GetMapping("/getProduct")
    public String getProduct(@RequestParam("prodNo") int prodNo ,
                             @SessionAttribute("user") User buyer,
                             @RequestParam(value = "wishCartNo", required = false) Integer wishCartNo,
                             @ModelAttribute("search") Search search ,
                             HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        System.out.println("/product/getProduct : GET");

        Product product = productService.getProduct(prodNo);

        String username = buyer.getUsername();
        model.addAttribute("username", username);

//        Purchase purchase = new Purchase();

        System.out.println("prodNo : " + prodNo);
        System.out.println("Buyer: " + buyer.getUsername());
        System.out.println("Username: " + username);


        Purchase purchase = new Purchase();
        purchase.setProdNo(prodNo);
        purchase.setUsername(username);
        int isWishExist = purchaseService.isProductInWishList(purchase);
        System.out.println("isWishExist : " + isWishExist);
        model.addAttribute("isWishExist", isWishExist);

        String categoryName = getCategoryNameByNo(product.getProdCateNo());

        Product cateName = new Product();
//        String prodCateName = cateName.setProdCateName(categoryName);
//        prodCateName = product.getProdCateName();
        search.setSearchKeyword(categoryName);
        System.out.println("categoryName : " + categoryName);

        search.setPageSize(pageSize);
//        search.setStartRowNum(1);
//        search.setListSize(30);
        search.setListSize(15);
        List<Product> productList = productService.getProductList(search);
        int prodTotalCnt = (productList.isEmpty())? 0 : productList.get(0).getProdTotalCnt();

        System.out.println(productList);

        Paging paging = new Paging(prodTotalCnt, search.getPage(), search.getPageSize(), search.getListSize());
        model.addAttribute("paging", paging);
        model.addAttribute("productList", productList);

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
        search.setPageSize(pageSize);
//        search.setListSize(30);
        search.setListSize(15);
        System.out.println("order: " + order);

        if(search.getPage() == 0) {
            search.setPage(1);
        }
//        search.setPageSize(pageSize);

        List<Product> productList = productService.getProductList(search);
        int prodTotalCnt = (productList.isEmpty())? 0 : productList.get(0).getProdTotalCnt();

        System.out.println(productList);

        Paging paging = new Paging(prodTotalCnt, search.getPage(), search.getPageSize(), search.getListSize());
        model.addAttribute("paging", paging);
        model.addAttribute("prodTotalCnt", prodTotalCnt);

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
