package com.shapeshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Autowired
    private PasswordUtils passwordUtils;

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordUtils.encryptPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String hashedRaw = passwordUtils.encryptPassword(rawPassword.toString());
        return hashedRaw.equals(encodedPassword);
    }
}