package com.placeHere.server.controller.pointShop;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.placeHere.server.domain.Point;
import com.placeHere.server.domain.Product;
import com.placeHere.server.domain.Purchase;
import com.placeHere.server.domain.Search;
import com.placeHere.server.service.pointShop.PointService;
import com.placeHere.server.service.pointShop.ProductService;
import com.placeHere.server.service.pointShop.PurchaseService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

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
//                                @RequestParam("username") String username,
                              @ModelAttribute("purchase") Purchase purchase,
                              Model model) throws Exception{

        String userName = "user01";

        System.out.println("/purchase/addPurchase : GET");

        int currPoint = pointService.getCurrentPoint(userName);

        Product product = productService.getProduct(prodNo);

        int tranPoint = product.getProdPrice()* product.getCntProd();
        purchase.setProdNo(prodNo);
        purchase.setTranPoint(tranPoint);
        purchase.setDepType("상품 구매");
        purchase.setRelNo(prodNo);

        System.out.println("product.getCntProd() = " + product.getCntProd());
        System.out.println("tranPoint : " + tranPoint);
        System.out.println("currPoint : " + currPoint);
        System.out.println("product.getProdPrice() : " + product.getProdPrice());

        model.addAttribute("currPoint", currPoint);
        model.addAttribute("userName", userName);
        model.addAttribute("tranPoint", tranPoint);
        model.addAttribute("product", product);
        model.addAttribute("purchase", purchase);

        return "pointShop/purchase/addPurchase";
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

    private void generateBarcode(String barcodeText, String filePath, int width, int height) throws Exception {
        BarcodeFormat format = BarcodeFormat.CODE_128;
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
//        hints.put(EncodeHintType.MARGIN, 5);  // 바코드 주변 여백

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(barcodeText, format, width, height, hints);

        File outputFile = new File(filePath);
        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", outputFile);  // 바코드 이미지를 PNG 파일로 저장
    }

    @PostMapping("/addPurchase")
    public String addPurchaseResult(@RequestParam("file") MultipartFile file,
                                    @ModelAttribute("purchase") Purchase purchase,
                                    @RequestParam("tranPoint") int tranPoint,
                                    Model model) throws Exception{

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
        generateBarcode(barcodeNumber, barcodeFilePath, 200, 100);  // 바코드 생성

        // 4. 바코드 파일명 저장
        purchase.setBarcodeName(barcodeFileName);  // 생성된 바코드 파일명을 상품에 설정

        // 5. 바코드 번호를 상품 객체에 추가
        purchase.setBarcodeNo(barcodeNumber);// 생성된 바코드 번호 저장
        purchase.setDepType("상품 구매");

        System.out.println("Barcode Number: " + barcodeNumber);  // 바코드 번호 출력
        System.out.println("Barcode File Name: " + barcodeFileName);  // 바코드 이미지 파일명 출력

        System.out.println("/purchase/addPurchase : POST");

        String userName = "user01";

        model.addAttribute("tranPoint", tranPoint);

        purchaseService.addPurchase(purchase);

        pointService.updatePoint(userName, -tranPoint);

        int currPoint = pointService.getCurrentPoint(userName);

        model.addAttribute("currPoint", currPoint);
        model.addAttribute("userName", userName);

        return "pointShop/purchase/addPurchaseResult";
    }

    @RequestMapping( value="listPurchase")
    public String listPurchase(@RequestParam("userName") String userName,
//                               @ModelAttribute("search") Search search ,
                               Model model) throws Exception {

        System.out.println("/purchase/listPurchase : GET / POST");

//        username = "user1";

        List<Purchase> purchaseList = purchaseService.getPurchaseList(userName);

        model.addAttribute("purchaseList", purchaseList);
        model.addAttribute("userName", userName);

        return "/pointShop/purchase/listPurchase";
    }

    @GetMapping("/getPurchase")
    public String getPurchase(@RequestParam("tranNo") int tranNo , Model model) throws Exception {

        System.out.println("/product/getPurchase : GET");

        String userName = "user01";

        Purchase purchase = purchaseService.getPurchase(tranNo);

        model.addAttribute("userName" , userName);
        model.addAttribute("purchase" , purchase);

        return "/pointShop/purchase/getPurchase";

    }

    @GetMapping("/listCart")
    public String getCartList(
//            @RequestParam("userName") String userName,
                              @ModelAttribute("purchase") Purchase purchase,
                              Model model) throws Exception {

        String userName = "user01";
        // 장바구니 목록을 서비스에서 받아옴
        List<Purchase> cartList = purchaseService.getCartList(userName);

        // 모델에 장바구니 목록을 추가하여 뷰로 전달
        model.addAttribute("cartList", cartList);

        // Thymeleaf 템플릿 이름을 반환
        return "pointShop/purchase/listCart"; // 템플릿 경로
    }

    // 찜 목록 조회
    @GetMapping("/listWish")
    public String getWishList(
//                          @RequestParam("userName") String userName,
                            @ModelAttribute("purchase") Purchase purchase,
                            Model model) throws Exception {

            String userName = "user01";
            // 찜 목록을 서비스에서 받아옴
        List<Purchase> wishList = purchaseService.getWishList(userName);

        // 모델에 찜 목록을 추가하여 뷰로 전달
        model.addAttribute("wishList", wishList);

        // Thymeleaf 템플릿 이름을 반환
        return "pointShop/purchase/listWish"; // 템플릿 경로
    }

    @RequestMapping("listPointHistory")
    public String getPointHistoryList(@RequestParam("userName") String userName,
                                      Model model) throws Exception {

        System.out.println("/purchase/listPurchase : GET / POST");

        System.out.println("userName: " + userName);

//        username = "user1";

        int currPoint = pointService.getCurrentPoint(userName);

        List<Point> pointHistoryList = pointService.getPointHistoryList(userName);

        model.addAttribute("currPoint", currPoint);
        model.addAttribute("pointHistoryList", pointHistoryList);
        model.addAttribute("userName", userName);

        return "pointShop/purchase/listPointHistory";
    }

}
