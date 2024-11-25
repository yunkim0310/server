package com.placeHere.server.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

//우선 시큐리티는 기존에 시큐리티 없이 로그인을 할 때는 개발자가 세션을 만들고 그 안에 정보를 넣는 것처럼 Security Session을 만든다.
//그렇지만 그 세션 안에는 아무 타입이나 들어갈 수 있는게 아니다.
//오직 Authentication 타입의 객체만 들어갈 수 있다. 그리고 Authentication 타입 안에는 UserDetails 타입의 객체가 들어갈 수 있다.


public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails ( User user ) {
        this.user = user;
    }


    // ROLE 값 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    // 계정이 만료 되었는지
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    // 계정이 잠겼는지
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        //
//        if ( user.getActiveStatus().equals("INACTIVE") ) {
//            System.out.println("INACTIVE USER ~~");
//            return false;
//        }


        return true;
    }
}