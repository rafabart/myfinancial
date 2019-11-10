package com.myfinancial.service.Impl;

import com.myfinancial.entity.User;
import com.myfinancial.exception.AuthenticationException;
import com.myfinancial.exception.UserEmailException;
import com.myfinancial.repository.UserRepository;
import com.myfinancial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User authenticate(String email, String password) {

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (!userOptional.isPresent()) {
            throw new AuthenticationException("Usuário não encontrado para o email informado!");
        }

        if (!userOptional.get().getPassword().equals(password)) {
            throw new AuthenticationException("Senha inválida!");
        }

        return userOptional.get();
    }


    @Override
    @Transient
    public User saveUser(User user) {
        validateEmail(user.getEmail());
        return userRepository.save(user);
    }


    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailException("Já existe um usuário cadastro com este email!");
        }
    }
}
