package com.placeHere.server.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

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

        return true;
    }
}