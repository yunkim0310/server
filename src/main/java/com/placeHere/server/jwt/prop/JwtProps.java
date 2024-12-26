package com.placeHere.server.jwt.prop;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml에 있는
 * secretkey를 가져오는 클래스
 */

@Data
@Component
@ConfigurationProperties("com.yunkim.jwt")
//@ConfigurationProperties("jwt")
public class JwtProps {

    private String secretKey;
//    private String secretKey;

    @PostConstruct
    public void init() {
        System.out.println("Loaded secretKey: " + secretKey);
    }

}
