package com.placeHere.server.service.user;

import com.placeHere.server.domain.User;

public interface UserService {

    public User getUser(String username) throws Exception;
}
