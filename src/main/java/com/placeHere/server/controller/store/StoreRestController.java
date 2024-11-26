package com.placeHere.server.controller.store;

import com.placeHere.server.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store/*")
public class StoreRestController {

    // Field
    @Autowired
    private StoreService storeService;




}
