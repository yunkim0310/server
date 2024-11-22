package com.placeHere.server;

import com.placeHere.server.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

//    @Test
//    public void testRemoveStore() {
//
//        storeService.removeStore(1);
//    }

    @Test
    public void testChkDuplicateBusinessNo() {
        int result = storeService.chkDuplicateBusinessNo("6575100638");

        System.out.println(result);
    }

}
