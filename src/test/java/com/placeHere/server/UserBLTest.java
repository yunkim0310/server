package com.placeHere.server;

import com.placeHere.server.domain.User;
import com.placeHere.server.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserBLTest {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Test
    public void test() {
        System.out.println("1234");
    }

    @Test
    public void getUser() throws Exception{
        System.out.println("4567");

        User user = new User();
        user = userService.getUser("place");
        System.out.println("user chk !! " + user);
        System.out.println("6789");
    }

}
