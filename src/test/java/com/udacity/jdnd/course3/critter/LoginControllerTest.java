package com.udacity.jdnd.course3.critter;

import com.udacity.jdnd.course3.critter.login.repository.UserRepository;
import com.udacity.jdnd.course3.critter.user.UserController;
import org.junit.Before;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;

public class LoginControllerTest {
    private UserController userController;

    private UserRepository usuarioRepositorio = mock(UserRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    //@Before
    @Before
    public void configuracion(){
        userController = new UserController();
        //Obtiene todos los campos inyectados en mi objeto controlador usuario.
        TestUtils.InyectarObjeto(userController, "userRepository", usuarioRepositorio);
        TestUtils.InyectarObjeto(userController, "bCryptPasswordEncoder", encoder);
    }
}
