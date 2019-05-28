package com.anvizent.schedulers.security;

import java.util.Date;

import com.datamodel.anvizent.service.model.User;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                Long.valueOf(user.getUserId()),
                user.getUserName(),
                user.getUserName(),
                user.getUserName(),
                user.getEmailId(),
                user.getPassword(),
                user.isActive(),
                new Date(10,10,2017)
        );
    }

}
