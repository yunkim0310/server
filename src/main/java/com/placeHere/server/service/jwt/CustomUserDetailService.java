package com.placeHere.server.service.jwt;

import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.CustomUser;
import com.placeHere.server.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 방식을 설정해 주기 위한 클래스이다.
 */

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("login - loadUserByUsername : " + username);

        User user = null;

        try {
            user = userDao.getUser(username);
        } catch (Exception e) {

            if ( user == null ) {

                log.info("사용자 없음,,, (일치하는 아이디가 없음)");
                throw new UsernameNotFoundException(" 사용자를 찾을 수 없습니다. " + username);
            }

        }

        // 존재하는 유저
        log.info("user : ");
//        log.info(user.toString());

        // Users -> CustomUser
        // 우리가 갖고 있는 Users 객체를 UserDetails에 맞게 변환해줘야 한다.
        // 시큐리티에서 사용할 수 있는 객체로 변환해줘야 한다.
        CustomUser customUser = new CustomUser(user);

        // 비밀번호 비교
//        log.info( "user의 비밀번호" + user.getPassword() );
//        log.info( "customUser 비밀번호" + customUser.getPassword() );

        log.info("customUser : ");
        log.info( customUser.toString() );

        return customUser;
    }


}
