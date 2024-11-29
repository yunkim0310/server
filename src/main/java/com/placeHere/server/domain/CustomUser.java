package com.placeHere.server.domain;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class CustomUser implements UserDetails {

    private User user;

    public CustomUser(User user) {
        this.user = user;
    }


    /**
     * Role 반환
     * @return
     */
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

    /**
     * 계정만료
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠겼는지
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 사용 가능한 계정인지
     * @return
     */
    @Override
    public boolean isEnabled() {
        // ACTIVE면 TRUE
        // 아니면 FALSE
//        return user.getActiveStatus() == "ACTIVE" ? true : false;
        return true;
    }

}
