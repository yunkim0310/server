package com.placeHere.server.service.jwt;

import com.placeHere.server.dao.user.UserDao;
import com.placeHere.server.domain.CustomUserDetails;
import com.placeHere.server.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user;

        try {

            user = userDao.getUser(username);

            if ( user != null ) {

                return new CustomUserDetails(user);
            }

        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }

        // active_status 확인
//        if ("INACTIVE".equals(user.getActiveStatus()) || "DELETED".equals(user.getActiveStatus())) {
//            throw new UsernameNotFoundException("Inactive or deleted account");
//        }

//        if

        return new CustomUserDetails(user);
    }
}
