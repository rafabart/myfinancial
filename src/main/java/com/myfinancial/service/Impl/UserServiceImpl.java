package com.myfinancial.service.Impl;

import com.myfinancial.entity.User;
import com.myfinancial.exception.UserEmailException;
import com.myfinancial.repository.UserRepository;
import com.myfinancial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User authenticate(String email, String password) {
        return null;
    }


    @Override
    public User saveUser(User user) {
        return null;
    }


    @Override
    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailException("Já existe um usuário cadastro com este email!");
        }
    }
}
