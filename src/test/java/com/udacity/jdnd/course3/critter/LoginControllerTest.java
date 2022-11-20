package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.login.LoginController;
import com.udacity.jdnd.course3.critter.login.domain.User;
import com.udacity.jdnd.course3.critter.login.repository.UserRepository;
import com.udacity.jdnd.course3.critter.login.requests.CreateUserRequest;
import com.udacity.jdnd.course3.critter.user.UserController;
import org.h2.command.ddl.CreateUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.RealSystem;
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

    private UserRepository usuarioRepositorio = mock(UserRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void configuracion(){
        loginController = new LoginController();
        //Obtiene todos los campos inyectados en mi objeto controlador usuario.
        TestUtils.InyectarObjeto(loginController, "userRepository", usuarioRepositorio);
        TestUtils.InyectarObjeto(loginController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserFine() throws Exception{
        when(encoder.encode("testPass")).thenReturn("thisIsHashed");
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
        assertEquals(0,usuarioNuevo.getId());
        assertEquals(request.getUserName(), usuarioNuevo.getUserName());
        assertEquals("thisIsHashed", usuarioNuevo.getPassword());

    }
}
