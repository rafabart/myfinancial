package com.myfinancial.repository;

import com.myfinancial.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@DataJpaTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    public void mustCheckIfEmailExist() {

        //Cenário
        User user = createUser();
        testEntityManager.persist(user);

        //Ação e execução
        boolean result = userRepository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void returnFalseIfEmailNotExist() {

        //Cenário, Ação e execução
        boolean result = userRepository.existsByEmail("usuario@email.com");

        //Verificação
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void mustSaveUserInDatabase() {
        //Cenário
        User user = createUser();

        //Ação e execução
        User userSaved = userRepository.save(user);

        //Verificação
        Assertions.assertThat(userSaved.getId()).isNotNull();
    }


    @Test
    public void mustFindUserByEmail() {

        User user = createUser();
        testEntityManager.persist(user);

        Optional<User> userOptional = userRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(userOptional.isPresent()).isTrue();
    }

    @Test
    public void mustReturnEmptyIfTryFindUserByEmailIfUserNotExist() {

        Optional<User> userOptional = userRepository.findByEmail("usuario@email.com");

        Assertions.assertThat(userOptional.isPresent()).isFalse();
    }


    public static User createUser() {
        return User
                .builder()
                .name("usuario")
                .email("usuario@email.com")
                .password("senha")
                .build();
    }
}
