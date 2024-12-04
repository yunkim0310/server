package com.placeHere.server.controller.pointShop;

import java.util.List;

import com.placeHere.server.service.pointShop.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


//==> 회원관리 RestController
@RestController
@RequestMapping("/product/*")
public class ProductRestController {

    ///Field
    @Autowired
    @Qualifier("productServiceImpl")
    private ProductService productService;

    public ProductRestController(){
        System.out.println(":: " + getClass().getSimpleName() + " default Constructor call\n");
    }

//    @RequestMapping(value="json/getProductAutocomplete/{prodName}", method = RequestMethod.GET)
    @GetMapping("/json/getProductAutocomplete/{prodName}")
    public List<String> getProductAutocomplete(@PathVariable("prodName") String prodName) throws Exception{

        System.out.println("RestController : /product/getProductAutocomplete : GET");

        System.out.println("Request received with prodName: " + prodName);

        return productService.getAutocomplete(prodName);
    }



}
