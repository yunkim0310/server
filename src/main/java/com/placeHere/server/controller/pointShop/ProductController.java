//package com.placeHere.server.controller.pointShop;
//
//import com.placeHere.server.domain.Product;
//import com.placeHere.server.domain.Search;
//import com.placeHere.server.service.pointShop.ProductService;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/product")
//public class ProductController {
//
//    @Autowired
//    @Qualifier("productServiceImpl")
//    private ProductService productService;
//
//    public ProductController(){
//
//        System.out.println("ProductController");
//
//    }
//
//    @Value("#{commonProperties['path']}")
//    String path;
//
//    @Autowired
//    ServletContext servletContext;
//
//    @RequestMapping( value="addProduct", method= RequestMethod.GET)
//    public String addProduct() throws Exception{
//
//        System.out.println("/product/addProductView : GET");
//
//        return "redirect:/product/addProduct.html";
//    }
//
//    @RequestMapping(value = "addProductResult", method = RequestMethod.POST)
//    public String addProductResult(@RequestParam("file") MultipartFile file,
//                             @ModelAttribute("product") Product product, Model model) throws Exception {
//        System.out.println("/product/addProduct : POST");
//
//        // 1. 상품 이미지 저장 (기존 로직)
//        String fileName = file.getOriginalFilename();
//        String uploadPath = servletContext.getRealPath(path);  // 이미지 저장 경로
//        String saveFile = uploadPath + fileName;
//        System.out.println("Image upload path: " + uploadPath);
//
//        try {
//            file.transferTo(new File(saveFile));
//            product.setProdImg1(fileName);  // 이미지 파일명 설정
//        } catch (IOException e) {
//            e.printStackTrace();
//            // 파일 업로드 실패 시 예외 처리
//        }
//
//        productService.addProduct(product);
//
//        // 상품 등록 후 이동할 JSP
//        return "forward:/product/addProductResult.html";
//    }
//
//    @RequestMapping( value="getProduct", method=RequestMethod.GET)
//    public String getProduct(@RequestParam("prodNo") int prodNo , HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
//
//        System.out.println("/product/getProduct : GET");
//
//        Product product = productService.getProduct(prodNo);
//
//        Cookie[] cookies = request.getCookies();
//        String history = "";
//
//        if (cookies!=null && cookies.length > 0) {
//            for (int i = 0; i < cookies.length; i++) {
//                Cookie cookie = cookies[i];
//                if (cookie.getName().equals("history")) {
//                    history = cookie.getValue();
//                }
//            }
//
//            history += "/" + String.valueOf(product.getProdNo());
//            Cookie cookie = new Cookie("history", history);
//            response.addCookie(cookie);
//
//        }else{
//
//            Cookie cookie = new Cookie("history", String.valueOf(product.getProdNo()));
//
//            response.addCookie(cookie);
//        }
//
//
//        model.addAttribute("product" , product);
//
//        return "forward:/product/getProduct.jsp";
//
//    }
//
//    @RequestMapping( value="updateProduct", method=RequestMethod.GET)
//    public String updateProduct( @RequestParam("prodNo") int prodNo , Model model) throws Exception {
//
//        System.out.println("/product/updateProductView : GET");
//
//        Product product = productService.getProduct(prodNo);
//
//        model.addAttribute("product" , product);
//
//        return "forward:/product/updateProduct.html";
//    }
//
//    @RequestMapping( value="updateProductResult", method=RequestMethod.POST)
//    public String updateProductResult( @RequestParam("file") MultipartFile file, @ModelAttribute("product") Product product , Model model, HttpSession session) throws Exception {
//
//        System.out.println("/product/updateProduct : POST");
//
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
//
//        //productService.updateProduct(product);
//
//        //return "redirect:/product/updateProduct?prodNo="+product.getProdNo();
//        return "forward:/product/updateProductResult.html";
//    }
//
//    @RequestMapping( value="listProduct")
//    public String listProduct(@RequestParam(value = "sortOrder", required = false) String sortOrder, @RequestParam("menu") String menu, @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception {
//
//        System.out.println("/product/listProduct : GET / POST");
//
//        System.out.println("searchPriceStart : " + search.getPriceMin());
//        System.out.println("searchPriceEnd : " + search.getPriceMax());
//
////        search.setSortOrder(sortOrder);
//        System.out.println("sortOrder: " + sortOrder);
//
//        if(search.getPage() == 0) {
//            search.setPage(1);
//        }
////        search.setPageSize(pageSize);
//
//        Map<String , Object> map = productService.getProductList(search);
//
////        Search resultPage = new Search( search.getPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit , pageSize);
////        System.out.println(resultPage);
//
//        model.addAttribute("menu", menu);
//        model.addAttribute("list", map.get("list"));
////        model.addAttribute("resultPage" , resultPage);
//        model.addAttribute("search" , search);
//
//        return "forward:/product/listProduct.jsp";
//    }
//
//
//}
