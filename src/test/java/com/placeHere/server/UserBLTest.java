package com.placeHere.server;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.placeHere.server.domain.User;
import com.placeHere.server.jwt.prop.JwtProps;
import com.placeHere.server.service.user.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class UserBLTest {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private JwtProps jwtProps;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void testPwd() {
        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
//        $2a$10$iuQb6RntW8zuMoHmPS3QiuKwem0153v/qhuR2lCe5RnaWMjD1Dw9a
//        $2a$10$aDZ8s5S.BxZguWj/qSeJEeuDoctV2/aOugZ2qUEGEIiOf44O9PhPC
    }

    @Test
    // 프로퍼티 파일 가져오기
    void testJwtProps() {
        String expected = "|+<T%0h;[G97|I$5Lr?h]}`8rUX.7;0gw@bF<R/|\"-U0n:_6j={'.T'GHs~<AxU9";
        assertEquals(expected, jwtProps.getSecretKey(), "시크릿키 못가져옴 ....... ");
    }

    //    @Test
    public void test() {
        System.out.println("1234");
    }

    //    @Test
    public void getUser() throws Exception{
        System.out.println("4567");

        User user = new User();
        user = userService.getUser("normal_user1");
        System.out.println("user chk !! " + user);
        System.out.println("6789");
    }

    //    @Test
    public void chkDuplication() throws Exception {

        String username = "normal_user1";

        boolean result = userService.chkDuplication(username);

        assertFalse(result, "중복회원 ~");
    }

    //    @Test
    public void join() throws Exception {

        User user = new User();
        user.setUsername("testName");
        user.setPassword("testUserName");
        user.setEmail("abc@naver.com");
        user.setRole("ROLE_USER");
//        user.setRegDt("111-2222-3333");
//        user.setProfileImg("��⵵");
//        user.setLoginDt("test@test.com");
//        user.setUpdateDt("test@test.com");
        user.setGender("M");
//        user.setBirth("test@test.com");
//        user.setActiveStatus("ACTIVE");

        userService.join(user);
        System.out.println( "회원가입 chk :: " + user);

        //==> console Ȯ��
        //System.out.println(user);
//
//        //==> API Ȯ��
//        Assert.assertEquals("testUserId", user.getUsername());
//        Assert.assertEquals("testUserName", user.getPassword());
//        Assert.assertEquals("testPasswd", user.getEmail());
//        Assert.assertEquals("111-2222-3333", user.getPhone());
//        Assert.assertEquals("��⵵", user.getAddr());
//        Assert.assertEquals("test@test.com", user.getEmail());
    }

    //    @Test
    public void updateLoginDt() throws Exception {

        String username = "testName";

        userService.updateLoginDt(username);
        User user = userService.getUser(username);

        System.out.println("로그인일시변경");
        System.out.println(user);
        ;
    }

    @Test
    public void getUserList() throws Exception {

        List<User> list = new ArrayList<>();

        list = userService.getUserList();

        System.out.println("유저 리스트 end");
        for (User user : list) {
            System.out.println( "list chk :: " + user);
        }
        System.out.println("유저 리스트 end");



//        System.out.println(userService.getUserList());

    }


}
