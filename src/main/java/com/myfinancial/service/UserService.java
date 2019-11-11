package com.myfinancial.service;

import com.myfinancial.entity.User;

import java.util.Optional;

public interface UserService {

    User authenticate(String email, String password);

    User saveUser(User user);

    void validateEmail(String email);

    Optional<User> findById(Long id);
}
