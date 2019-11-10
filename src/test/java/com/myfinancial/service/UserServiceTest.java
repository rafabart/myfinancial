package com.myfinancial.service;

import com.myfinancial.entity.User;
import com.myfinancial.exception.AuthenticationException;
import com.myfinancial.exception.UserEmailException;
import com.myfinancial.repository.UserRepository;
import com.myfinancial.service.Impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @SpyBean
    private UserServiceImpl userServiceImpl;

    @MockBean
    private UserRepository userRepository;


    //expected = Test.None.class -> Valida o método se não ocorrer uma excessão.
    @Test(expected = Test.None.class)
    public void mustValidateEmail() {
        //Cenário

        //Quando chamar o método userRepository.existsByEmail (passando qualquer String) retorne false
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //Ação
        userServiceImpl.validateEmail("email@email.com");
    }


    //expected = UserEmailException.class - > Valida o método se ocorrer uma excessão do tipo UserEmailException.
    @Test(expected = UserEmailException.class)
    public void mustThrowExceptionWhenTryToValidateEmailThatAlreadyExists() {
        //Cenário
        Mockito.when(userRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //Ação
        userServiceImpl.validateEmail("usuario@email.com");
    }


    @Test(expected = Test.None.class)
    public void mustAuthenticateUserWithSuccess() {
        final String email = "email@email.com";
        final String password = "senha";

        User user = User.builder().email(email).password(password).id(1L).build();

        //Quando chamar o método userRepository.findByEmail(email) retorne um Optional com objeto user dentro
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User userSaved = userServiceImpl.authenticate(email, password);

        Assertions.assertThat(userSaved).isNotNull();
    }


    @Test
    public void mustThrowExceptionWhenTryToFindUserWithEmailNonexistent() {

        final String message = "Usuário não encontrado para o email informado!";

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(
                () -> userServiceImpl.authenticate("email@email.com", "senha"));

        Assertions.assertThat(exception).isInstanceOf(AuthenticationException.class).hasMessage(message);
    }


    @Test
    public void mustThrowExceptionWhenPasswordIsWrong() {

        final String password = "senha";
        final String message = "Senha inválida!";

        User user = User.builder().email("email@email.com").password(password).id(1L).build();

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        Throwable exception = Assertions.catchThrowable(
                () -> userServiceImpl.authenticate("email@email.com", "senhaErrada"));

        Assertions.assertThat(exception).isInstanceOf(AuthenticationException.class).hasMessage(message);
    }


    @Test(expected = Test.None.class)
    public void mustSaveUserWithSucess() {

        //Não faça nada ao tentar validar email.
        Mockito.doNothing().when(userServiceImpl).validateEmail(Mockito.anyString());

        User user = User.builder().name("Nome").email("email@email.com").password("senha").id(1L).build();
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User userSaved = userServiceImpl.saveUser(new User());

        Assertions.assertThat(userSaved).isNotNull();
        Assertions.assertThat(userSaved.getId()).isEqualTo(1L);
        Assertions.assertThat(userSaved.getName()).isEqualTo("Nome");
        Assertions.assertThat(userSaved.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(userSaved.getPassword()).isEqualTo("senha");
    }

    @Test(expected = UserEmailException.class)
    public void mustNotSaveUserWithExistingEmail() {

        final String email = "email@email.com";
        Mockito.doThrow(UserEmailException.class).when(userServiceImpl).validateEmail(email);

        User user = User.builder().email("email@email.com").build();

        userServiceImpl.saveUser(user);

        //Verifica se userRepository.save(email) nunca foi executado na instância userServiceImpl
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }
}
