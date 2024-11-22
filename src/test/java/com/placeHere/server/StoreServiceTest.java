package com.placeHere.server;

import com.placeHere.server.service.store.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    @Qualifier("storeServiceImpl")
    private StoreService storeService;

    @Test
    public void testRemoveStore() {

        storeService.removeStore(1);
    }

}
