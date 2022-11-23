package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.login.LoginController;
import com.udacity.jdnd.course3.critter.login.domain.User;
import com.udacity.jdnd.course3.critter.login.repository.UserRepository;
import com.udacity.jdnd.course3.critter.login.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController loginController;

    private UserRepository userRepository = mock(UserRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void configuracion(){
        loginController = new LoginController();
        //Obtiene todos los campos inyectados en mi objeto controlador usuario.
        TestUtils.InyectarObjeto(loginController, "userRepository", userRepository);
        TestUtils.InyectarObjeto(loginController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void createUserFine() throws Exception{
        when(bCryptPasswordEncoder.encode("testPass")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName("test");
        request.setPassword("testPass");
        request.setConfirmPassword("testPass");

        //Metodo a probar
        ResponseEntity<User> response = loginController.createUser(request);
        //Afirmaciones sobre objeto q hace las solicitudes
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //Afirmaciones sobre el usuario creado
        User usuarioNuevo = response.getBody();

        assertNotNull(usuarioNuevo);
        assertEquals(Optional.of(0),usuarioNuevo.getId());
        assertEquals(request.getUserName(), usuarioNuevo.getUserName());
        assertEquals("thisIsHashed", usuarioNuevo.getPassword());
    }

    @Test
    public void contraseniaDebil(){
        String PASSWORDDEBIL = "123";
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName("tesUser");
        request.setPassword(PASSWORDDEBIL);
        request.setConfirmPassword(PASSWORDDEBIL);

        ResponseEntity<User> response = loginController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void contraseniaNoConfirmada(){
        String PASSWORD = "pass";
        CreateUserRequest request = new CreateUserRequest();
        request.setUserName("Kevin");
        request.setPassword(PASSWORD);
        request.setConfirmPassword("NOIGUAL");

        ResponseEntity<User> response = loginController.createUser(request);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
