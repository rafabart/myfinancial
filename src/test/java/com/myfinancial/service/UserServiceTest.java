package com.myfinancial.service;

import com.myfinancial.entity.User;
import com.myfinancial.exception.UserEmailException;
import com.myfinancial.repository.UserRepository;
import com.myfinancial.service.Impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
    }


    //expected = UserEmailException.class -> Valida o método se não ocorrer uma excessão.
    @Test(expected = Test.None.class)
    public void mustValidateEmail() {
        //Cenário
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //Ação
        userService.validateEmail("email@email.com");
    }


    //expected = UserEmailException.class - > Valida o método se ocorrer uma excessão do tipo UserEmailException.
    @Test(expected = UserEmailException.class)
    public void mustThrowExceptionWhenTryToValidateEmailThatAlreadyExists() {
        //Cenário
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //Ação
        userService.validateEmail("usuario@email.com");
    }
}
