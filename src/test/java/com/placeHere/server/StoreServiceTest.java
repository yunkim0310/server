package com.placeHere.server;

import com.placeHere.server.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreServiceTest {

//    @Autowired
//    @Qualifier("storeServiceImpl")
//    private StoreService storeService;
//
//    @Test
//    public void testRemoveStore() {
//
//        storeService.removeStore(1);
//    }

    @Test
    public void test() {

        java.util.Date utilDate = new java.util.Date();

        // java.util.Date를 java.sql.Date로 변환
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        // 결과 출력
        System.out.println("현재 날짜: " + sqlDate);

    }

}
