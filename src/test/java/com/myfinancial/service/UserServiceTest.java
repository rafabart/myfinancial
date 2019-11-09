package com.myfinancial.service;

import com.myfinancial.entity.User;
import com.myfinancial.exception.UserEmailException;
import com.myfinancial.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @Test(expected = Test.None.class)
    public void mustValidateEmail() {
        //Cenário
        userRepository.deleteAll();

        //Ação
        userService.validateEmail("email@email.com");
    }

    @Test(expected = UserEmailException.class)
    public void mustThrowExceptionWhenTryToValidateEmailThatAlreadyExists() {
        //Cenário
        User user = User.builder().name("usuario").email("usuario@email.com").build();
        userRepository.save(user);

        //Ação
        userService.validateEmail("usuario@email.com");
    }
}
