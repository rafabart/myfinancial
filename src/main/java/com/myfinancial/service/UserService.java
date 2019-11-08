package com.myfinancial.service;

import com.myfinancial.entity.User;

public interface UserService {

    User authenticate(String email, String password);

    User saveUser(User user);

    void validateEmail(String email);
}
