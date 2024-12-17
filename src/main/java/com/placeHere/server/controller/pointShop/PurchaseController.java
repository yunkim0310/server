package com.placeHere.server.controller.pointShop;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.placeHere.server.domain.*;
import com.placeHere.server.service.aws.AwsS3Service;
import com.placeHere.server.service.pointShop.PointService;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.pointShop.PurchaseService;
import com.placeHere.server.service.user.UserService;
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


//import org.springframework.mock.web.MockMultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {

    @Autowired
    @Qualifier("purchaseServiceImpl")
    private PurchaseService purchaseService;

    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    @Autowired
    @Qualifier("pointServiceImpl")
    private PointService pointService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private AwsS3Service awsS3Service;

    @Value("${cloud.aws.s3.bucket-url}")
    private String bucketUrl;

    @Value("${page_size}")
    private int pageSize;

    @Value("${list_size}")
    private int listSize;

    // Constructor
    public PurchaseController() {
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }

//    @Value("${barcode_image_path}")
//    String barcodePath;

    @Autowired
    ServletContext servletContext;

    @GetMapping("/addPurchase")
    public String addPurchase(@RequestParam("prodNo") int prodNo,
//                                @SessionAttribute("user") User buyer,
                              HttpSession session,
                              @ModelAttribute("purchase") Purchase purchase,
                              Model model) throws Exception{
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
            String username = user.getUsername();
            purchase.setBuyer(user);
            purchase.setUsername(username);
            System.out.println("addPurchase's username: " + username);

            System.out.println("/purchase/addPurchase : GET");

            int currPoint = pointService.getCurrentPoint(username);

            Product product = productService.getProduct(prodNo);

            int tranPoint = product.getProdPrice() * product.getCntProd();
            purchase.setProdNo(prodNo);
            purchase.setTranPoint(-tranPoint);
            purchase.setDepType("상품 구매");
            purchase.setRelNo(prodNo);

            System.out.println("product.getCntProd() = " + product.getCntProd());
            System.out.println("tranPoint : " + tranPoint);
            System.out.println("currPoint : " + currPoint);
            System.out.println("product.getProdPrice() : " + product.getProdPrice());
//
            model.addAttribute("url", bucketUrl);
            model.addAttribute("currPoint", currPoint);
            model.addAttribute("username", username);
            model.addAttribute("tranPoint", tranPoint);
            model.addAttribute("product", product);
            model.addAttribute("purchase", purchase);

            return "pointShop/purchase/addPurchase";
        }else{
            return "redirect:/";
        }
    }

    private String generateBarcodeNumber() {
        // 현재 시간을 밀리초 단위로 가져와 12자리로 만들기
        String barcode = String.format("%012d", System.currentTimeMillis() % 1000000000000L);
        // 마지막 1자리는 체크디지털로 계산
        int checkDigit = calculateCheckDigit(barcode);
        return barcode + checkDigit;  // 13자리 바코드 번호 반환
    }

    private int calculateCheckDigit(String barcode) {
        int sum = 0;
        for (int i = 0; i < barcode.length(); i++) {
            int digit = Character.getNumericValue(barcode.charAt(i));
            if (i % 2 == 0) {
                sum += digit;  // 홀수 자리(0,2,4,...)는 그대로 더함
            } else {
                sum += digit * 3;  // 짝수 자리(1,3,5,...)는 3배로 더함
            }
        }
        return (10 - (sum % 10)) % 10;  // 10으로 나눈 나머지를 이용해 체크디지털 계산
    }

    private String generateBarcode(String barcodeText, String filePath, int width, int height) throws Exception {

        BarcodeFormat format = BarcodeFormat.CODE_128;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//        hints.put(EncodeHintType.MARGIN, 5);  // 바코드 주변 여백

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(barcodeText, format, width, height, hints);

        File outputFile = new File(filePath);
        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", outputFile);  // 바코드 이미지를 PNG 파일로 저장

        Map<String, String> result = awsS3Service.uploadFile(outputFile, "point/barcode/");

        return result.get("filePath");
    }


//    private String generateBarcode(String barcodeText, String filePath, int width, int height) throws Exception {
//        BarcodeFormat format = BarcodeFormat.CODE_128;
//        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//
//        MultiFormatWriter writer = new MultiFormatWriter();
//        BitMatrix bitMatrix = writer.encode(barcodeText, format, width, height, hints);
//
//        File outputFile = new File(filePath);
//        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", outputFile);  // 바코드 이미지를 PNG 파일로 저장
//
//        // File을 MultipartFile로 변환
//        MultipartFile multipartFile = convertFileToMultipartFile(outputFile);
//
//        // multipartFile을 AWS S3에 업로드
//        Map<String, String> result = awsS3Service.uploadFile(multipartFile, "point/barcode/");
//
//        return result.get("filePath");
//    }

//    private MultipartFile convertFileToMultipartFile(File file) throws IOException {
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            // "file"는 MultipartFile의 이름이고, "image.png"는 업로드되는 파일의 이름입니다.
//            return new MockMultipartFile("file", file.getName(), "image/png", fileInputStream);
//        }
//    }


    @PostMapping("/addPurchase")
    public String addPurchaseResult(@RequestParam("file") MultipartFile file,
                                    @ModelAttribute("purchase") Purchase purchase,
                                    @RequestParam("tranPoint") int tranPoint,
                                    @SessionAttribute("user") User buyer,
                                    @ModelAttribute("point") Point point,
                                    Model model) throws Exception{

        String username = buyer.getUsername();
        purchase.setBuyer(buyer);
        purchase.setUsername(username);
        System.out.println("/purchase/addPurchase : POST");
        System.out.println("addPurchaseResult's username = " + username);

        Product product = productService.getProduct(purchase.getProdNo());

        purchase.setPurchaseProd(product); // purchaseProd 객체가 null이 아니게 설정
        model.addAttribute("product", product);
        model.addAttribute("purchase", purchase);

        String fileName = file.getOriginalFilename();
        String uploadPath = "C:/WorkSpace/placeHere/server/src/main/resources/static/file/pointShop";

        File barcodeDirectory = new File(uploadPath);
        if (!barcodeDirectory.exists()) {
            barcodeDirectory.mkdirs();
        }

        String saveFile = uploadPath + fileName;
        System.out.println("Image upload path: " + uploadPath);

        try {
            file.transferTo(new File(saveFile));
            purchase.setBarcodeName(fileName);
        } catch (IOException e) {
            e.printStackTrace();

        }

        String barcodeNumber = generateBarcodeNumber();
        String barcodeFileName = barcodeNumber + ".png";
        String barcodeFilePath = uploadPath + "/" + barcodeFileName;




        System.out.println("barcodeDirectory : "+ barcodeDirectory);

        // 3. 바코드 이미지 생성
        String fileNam = generateBarcode(barcodeNumber, barcodeFilePath, 200, 100);  // 바코드 생성

        // 4. 바코드 파일명 저장
        purchase.setBarcodeName(fileNam);  // 생성된 바코드 파일명을 상품에 설정

        // 5. 바코드 번호를 상품 객체에 추가
        purchase.setBarcodeNo(barcodeNumber);// 생성된 바코드 번호 저장
        purchase.setDepType("상품 구매");

        System.out.println("Barcode Number: " + barcodeNumber);  // 바코드 번호 출력
        System.out.println("Barcode File Name: " + barcodeFileName);  // 바코드 이미지 파일명 출력

//        AwsS3Service s3Service = new AwsS3Service(bucketUrl);
//        s3Service.uploadFile(barcodeFileName, barcodeFilePath);

        System.out.println("/purchase/addPurchase : POST");


        point.setTranPoint(-tranPoint);

//        point.setCurrPoint(point.getCurrPoint()-tranPoint);

        model.addAttribute("url", bucketUrl);

        model.addAttribute("tranPoint", tranPoint);

        purchaseService.addPurchase(purchase);

        pointService.updatePoint(point);

        int currPoint = pointService.getCurrentPoint(username);

        model.addAttribute("currPoint", currPoint);
        model.addAttribute("username", username);

//        return "pointShop/purchase/addPurchaseResult";
//        return "pointShop/purchase/getPurchase";
        return "redirect:/purchase/listPurchase";
    }

    @RequestMapping( value="listPurchase")
    public String listPurchase(HttpSession session,
//            @SessionAttribute("user") User buyer,
                               @RequestParam(value = "order", required = false) String order,
                               @ModelAttribute("search") Search search ,
                               Model model) throws Exception {

        System.out.println("/purchase/listPurchase : GET / POST");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
            String username = user.getUsername();

            System.out.println("username's listPurchase : " + username);

            search.setUsername(username);
            search.setOrder(order);
            search.setPageSize(pageSize);
//        search.setListSize(30);
            search.setListSize(5);
            System.out.println("order: " + order);

            if (search.getPage() == 0) {
                search.setPage(1);
            }

            List<Purchase> purchaseList = purchaseService.getPurchaseList(search);
            int prodTotalCnt = (purchaseList.isEmpty()) ? 0 : purchaseList.get(0).getPurchaseTotalCnt();

            Paging paging = new Paging(prodTotalCnt, search.getPage(), search.getPageSize(), search.getListSize());
            model.addAttribute("url", bucketUrl);
            model.addAttribute("paging", paging);
            model.addAttribute("search", search);
            model.addAttribute("prodTotalCnt", prodTotalCnt);

            model.addAttribute("purchaseList", purchaseList);
            model.addAttribute("username", username);

            return "/pointShop/purchase/listPurchase";
        }else{
            return "redirect:/";
        }
    }

    @GetMapping("/getPurchase")
    public String getPurchase(HttpSession session,
//                                @SessionAttribute("user") User buyer,
                                @RequestParam("tranNo") int tranNo , Model model) throws Exception {

        System.out.println("/product/getPurchase : GET");
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
//        String username = buyer.getUsername();
        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
            Purchase purchase = new Purchase();
//        System.out.println("Buyer: " + purchase.getBuyer());
//        System.out.println("Username: " + purchase.getBuyer().getUsername());

            purchase = purchaseService.getPurchase(tranNo);

//        model.addAttribute("username" , username);
            model.addAttribute("url", bucketUrl);
            model.addAttribute("purchase", purchase);

            return "/pointShop/purchase/getPurchase";
        }else{
            return "redirect:/";
        }

    }

    @GetMapping("/listCart")
    public String getCartList(HttpSession session,
//            @SessionAttribute("user") User buyer,
                              @ModelAttribute("purchase") Purchase purchase,
                              Model model) throws Exception {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
            String username = user.getUsername();
            purchase.setBuyer(user);
            purchase.setUsername(username);
            System.out.println("user's carlist : " + username);
            // 장바구니 목록을 서비스에서 받아옴
            List<Purchase> cartList = purchaseService.getCartList(username);

            int tranPoint = 0;
            int numItems = cartList.size();
            int currPoint = pointService.getCurrentPoint(username);
            model.addAttribute("currPoint", currPoint);
            System.out.println("보유 포인트 : " + currPoint);

            // 첫 번째 상품만 따로 추출하고 나머지 상품은 '외 N개'로 표현
            Purchase firstItem = cartList.isEmpty() ? null : cartList.get(0);
            String additionalItemsText = (numItems > 1) ? "외 " + (numItems - 1) + "개" : "";

            // 각 상품에 대해 결제 포인트 계산
            for (Purchase cartItem : cartList) {
                Product product = productService.getProduct(cartItem.getProdNo());
                Point point = new Point();
                int plusPoint = product.getProdPrice();
                cartItem.setProdNo(cartItem.getProdNo());
                cartItem.setTranPoint(-plusPoint);  // 포인트 차감
//            cartItem.setDepType("상품 구매");
                cartItem.setRelNo(cartItem.getProdNo());
                tranPoint += plusPoint;
//            purchaseService.addPurchase(purchase);
            }
            purchase.setTranPoint(tranPoint);  // 전체 총 결제 금액에 대한 포인트 차감
            model.addAttribute("url", bucketUrl);
            model.addAttribute("purchase", purchase);
            model.addAttribute("tranPoint", tranPoint);
            model.addAttribute("cartList", cartList);
            model.addAttribute("firstItem", firstItem);
            model.addAttribute("additionalItemsText", additionalItemsText);

//        purchaseService.purchaseProducts(username);

            // 모델에 장바구니 목록을 추가하여 뷰로 전달
            model.addAttribute("cartList", cartList);
            model.addAttribute("username", username);

            // Thymeleaf 템플릿 이름을 반환
            return "pointShop/purchase/listCart"; // 템플릿 경로
        }else{
            return "redirect:/";
        }
    }

    @PostMapping("/addPurchaseCart")
    public String addPurchaseCartResult(@RequestParam("file") MultipartFile file,
                                        @RequestParam("tranPoint") int tranPoint,
                            //            @RequestParam("cartList")  List<Purchase> purchaseList,  // List<Purchase>를 받을 수 있도록 설정
                                        @SessionAttribute("user") User buyer,
                                        @ModelAttribute("point") Point point,
                                        Model model) throws Exception {

        String username = buyer.getUsername();
        int currPoint = pointService.getCurrentPoint(username);
//        System.out.println("addPurchaseCartResult's currPoint : "+currPoint);
        System.out.println("point.getCurrPoint : "+currPoint);

        // 구매한 상품들을 처리
//        List<Product> products = new ArrayList<>();
        List<Purchase> purchaseList = purchaseService.getCartList(buyer.getUsername());
        System.out.println("purchaseList : "+purchaseList.size());
        for (Purchase purchase : purchaseList) {
            purchase.setBuyer(buyer);
            purchase.setUsername(username);
            point.setUsername(username);
            System.out.println("purchaseList : "+purchaseList.size());
            Product product = productService.getProduct(purchase.getProdNo());
            purchase.setPurchaseProd(product);
//            products.add(product);  // 상품 목록에 추가

            currPoint = pointService.getCurrentPoint(username);
            System.out.println("addPurchaseCartResult's currPoint : "+currPoint);

            purchase.setCurrPoint(currPoint);
            point.setCurrPoint(currPoint);

            // 바코드 생성 및 파일 업로드 처리
            String fileName = file.getOriginalFilename();
            String uploadPath = "C:/WorkSpace/placeHere/server/src/main/resources/static/file/pointShop";
            File barcodeDirectory = new File(uploadPath);
            if (!barcodeDirectory.exists()) {
                barcodeDirectory.mkdirs();
            }

            String saveFile = uploadPath + fileName;
            try {
                file.transferTo(new File(saveFile));
                purchase.setBarcodeName(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String barcodeNumber = generateBarcodeNumber();
            System.out.println("barcodeNumber : "+barcodeNumber);
            String barcodeFileName = barcodeNumber + ".png";
            String barcodeFilePath = uploadPath + "/" + barcodeFileName;

            // 바코드 이미지 생성
            String fileNam = generateBarcode(barcodeNumber, barcodeFilePath, 200, 100);
            System.out.println("fileNam : "+fileNam);

            int buyPoint = product.getProdPrice();
            // 바코드 파일명 저장
            purchase.setBarcodeName(fileNam);
            purchase.setBarcodeNo(barcodeNumber);  // 바코드 번호 저장
            purchase.setDepType("상품 구매");
            purchase.setTranPoint(buyPoint);
            point.setTranPoint(-buyPoint);  // 총 결제 포인트 차감
            System.out.println("addPurchaseCartResult : "+ -buyPoint);
            // 포인트 업데이트
            purchaseService.addPurchase(purchase);
            pointService.updatePoint(point);
            int changePoint = pointService.getCurrentPoint(username);
            point.setCurrPoint(changePoint);
            System.out.println("changePoint : "+changePoint);
//            point.setCurrPoint(currPoint);
//            System.out.println("test currPoint : "+point.getCurrPoint());

        }

        purchaseService.purchaseProducts(username);

        // 최종 결제 포인트 차감
        model.addAttribute("url", bucketUrl);
        model.addAttribute("currPoint", currPoint);
        model.addAttribute("tranPoint", tranPoint);
        model.addAttribute("purchase", purchaseList);
//        model.addAttribute("products", products);

        model.addAttribute("username", username);

//        return "pointShop/purchase/addPurchaseCartResult";  // 결과 페이지로 이동
        return "redirect:/purchase/listPurchase";
    }




    // 찜 목록 조회
    @GetMapping("/listWish")
    public String getWishList(HttpSession session,
//                            @SessionAttribute("user") User buyer,
                            @ModelAttribute("purchase") Purchase purchase,
                            Model model) throws Exception {

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
            String username = user.getUsername();
            // 찜 목록을 서비스에서 받아옴
            List<Purchase> wishList = purchaseService.getWishList(username);

            // 모델에 찜 목록을 추가하여 뷰로 전달
            model.addAttribute("url", bucketUrl);
            model.addAttribute("wishList", wishList);
            model.addAttribute("username", username);

            // Thymeleaf 템플릿 이름을 반환
            return "pointShop/purchase/listWish"; // 템플릿 경로
        }else{
            return "redirect:/";
        }
    }

    @RequestMapping("listPointHistory")
    public String getPointHistoryList(HttpSession session,
//            @SessionAttribute("user") User buyer,
                                      @ModelAttribute("search") Search search ,
                                      Model model) throws Exception {

        System.out.println("/purchase/listPurchase : GET / POST");

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user == null) {

            return "redirect:/";

        }else if (user.getRole().equals("ROLE_USER")) {
            Point point = new Point();
            point.setBuyer(user);
            String username = user.getUsername();
            point.setUsername(username);
            search.setUsername(username);
            search.setPageSize(pageSize);
//        search.setListSize(30);
            search.setListSize(15);

            if (search.getPage() == 0) {
                search.setPage(1);
            }

            if ("차감".equals(search.getSearchKeyword())) {
                search.setSearchKeyword("차감");
            } else if ("적립".equals(search.getSearchKeyword())) {
                search.setSearchKeyword("적립");
            }

            System.out.println("keyword : " + search.getSearchKeyword());

            System.out.println("username: " + username);

            int currPoint = pointService.getCurrentPoint(username);

            List<Point> pointHistoryList = pointService.getPointHistoryList(search);
            int prodTotalCnt = (pointHistoryList.isEmpty()) ? 0 : pointHistoryList.get(0).getPointTotalCnt();

            Paging paging = new Paging(prodTotalCnt, search.getPage(), search.getPageSize(), search.getListSize());
            model.addAttribute("paging", paging);
            model.addAttribute("search", search);
            model.addAttribute("prodTotalCnt", prodTotalCnt);

            model.addAttribute("currPoint", currPoint);
            model.addAttribute("pointHistoryList", pointHistoryList);
            model.addAttribute("username", username);

            return "pointShop/purchase/listPointHistory";
        }else{
            return "redirect:/";
        }
    }

    // 선택된 상품들의 총 가격 계산
    @PostMapping("/calculateTotal")
    @ResponseBody
    public double calculateTotal(@RequestBody List<Purchase> selectedItems) {
        double total = 0;
        for (Purchase purchase : selectedItems) {
            if (purchase.getSelected() != 0) {
                total += purchase.getPurchaseProd().getProdPrice() * purchase.getPurchaseProd().getCntProd();
            }
        }
        return total;
    }

    // 선택된 상품들 일괄 구매 처리
    @PostMapping("/buy")
    public String buySelectedItems(@SessionAttribute("user") User buyer, @RequestBody List<Purchase> selectedItems, Model model) throws Exception{

        String username = buyer.getUsername();

        model.addAttribute("username", username);

        purchaseService.buySelectedItems(selectedItems);
        return "pointShop/purchase/listCart"; // 구매 후 장바구니 목록 페이지로 이동
    }

    // 선택된 상품들 삭제
    @PostMapping("/removeSelected")
    public String removeSelectedItems(@SessionAttribute("user") User buyer, @RequestBody List<Purchase> selectedItems, Model model) throws Exception{

        String username = buyer.getUsername();

        model.addAttribute("username", username);

        purchaseService.removeSelectedItems(selectedItems);
        return "pointShop/purchase/listCart";
    }

//    @PostMapping("/addPurchaseCart")
//    public String addPurchaseCart(@SessionAttribute("user") User buyer, Model model) throws Exception{
//
//        String username = buyer.getUsername();
//        List<Purchase> cartItems = purchaseService.getCartList(buyer.getUsername());  // 장바구니의 모든 상품을 가져옴
//        int totalTranPoint = 0;
//        int numItems = cartItems.size();
//        int currPoint = pointService.getCurrentPoint(username);
//
//        // 첫 번째 상품만 따로 추출하고 나머지 상품은 '외 N개'로 표현
//        Purchase firstItem = cartItems.isEmpty() ? null : cartItems.get(0);
//        String additionalItemsText = (numItems > 1) ? "외 " + (numItems - 1) + "개" : "";
//
//        // 각 상품에 대해 결제 포인트 계산
//        for (Purchase cartItem : cartItems) {
//            Product product = productService.getProduct(cartItem.getProdNo());
//            Point point = new Point();
//            int tranPoint = product.getProdPrice() * cartItem.getCntProd();
//            point.setTranPoint(-tranPoint);  // 포인트 차감
//            point.setDepType("상품 구매");
//            point.setRelNo(cartItem.getProdNo());
//            totalTranPoint += tranPoint;
//        }
//        Purchase purchase = new Purchase();
//        purchase.setTranPoint(-totalTranPoint);  // 전체 총 결제 금액에 대한 포인트 차감
//        model.addAttribute("totalTranPoint", totalTranPoint);
//        model.addAttribute("currPoint", currPoint);
//        model.addAttribute("cartItems", cartItems);
//        model.addAttribute("firstItem", firstItem);
//        model.addAttribute("additionalItemsText", additionalItemsText);
//
//        purchaseService.purchaseProducts(username);
//
//        return "pointShop/purchase/listPurchase";  // 구매 내역 페이지로 리다이렉트
//    }

}
