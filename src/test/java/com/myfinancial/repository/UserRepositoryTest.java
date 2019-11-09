package com.myfinancial.repository;

import com.myfinancial.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void mustCheckIfEmailExist() {

        //Cenário
        User user = User.builder().name("usuario").email("usuario@email.com").build();
        userRepository.save(user);

        //Ação e execução
        boolean result = userRepository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void returnFalseIfEmailNotExist(){
        //Cenário
        userRepository.deleteAll();

        //Ação e execução
        boolean result = userRepository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isFalse();
    }
}
