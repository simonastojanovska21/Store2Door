package com.example.store.service;

import com.example.store.model.User;
import com.example.store.model.enums.Role;

public interface UserService {
    User register(String username, String password, String repeatedPassword, String name, String surname, String address, String city, String telephone, String image, Role role);
    User findByUsername(String username);
}
