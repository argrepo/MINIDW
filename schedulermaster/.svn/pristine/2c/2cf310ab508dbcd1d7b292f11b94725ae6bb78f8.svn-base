package com.anvizent.scheduler.security;

import com.anvizent.scheduler.model.User;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getEnabled(),
                user.getLastPasswordResetDate()
        );
    }

}
