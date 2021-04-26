package com.example.store.service.impl;

import com.example.store.model.User;
import com.example.store.model.enums.Role;
import com.example.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Service;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp {

    @Autowired
    private UserRepository userRepository;

    @Override
    public String execute(Connection<?> connection) {
        final User user = new User();
        user.setUsername(connection.getDisplayName());
        user.setPassword("Store123");
        user.setRole(Role.FACEBOOK_USER);
        userRepository.save(user);
        return user.getUsername();
    }

}