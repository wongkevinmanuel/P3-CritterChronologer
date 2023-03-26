package com.udacity.jdnd.course3.critter.login;

import com.udacity.jdnd.course3.critter.login.domain.User;
import com.udacity.jdnd.course3.critter.login.repository.UserRepository;
import com.udacity.jdnd.course3.critter.login.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth/user")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private String existenNull(String user, String pass, String confPass){
        if(user.isEmpty())
            return "User name is length 0.";

        if(pass.isEmpty())
            return "Password is length 0.";

        if (pass.isEmpty())
            return "Confirmd Password is length 0.";

        return "";
    }

    private boolean esTamanioUserIncorrecto(String userName ,int tamanioInferior, int tamanioSuperior){
        if (userName.length() < tamanioInferior || userName.length() >tamanioSuperior){
            return true;
        }
        return false;
    }

    private boolean esPasswordErrado(String password, int tamanioInferior) {
        if (password.isEmpty() || password.length() <= tamanioInferior) {
            return true;
        }
        return false;
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request){
        if(Objects.isNull(request)) {
            log.error("No null Request. Cannot create user.", new Exception());
            return ResponseEntity.badRequest().build();
        }

        String camposNull = existenNull(request.getUserName(), request.getPassword()
                                        , request.getConfirmPassword());

        if (!camposNull.isEmpty()){
            log.error("No null in "+ camposNull,new Exception("Null in field "+camposNull));
            return ResponseEntity.badRequest().build();
        }

        if(esTamanioUserIncorrecto(request.getUserName(), 6, 32)){
            String errorTamanioUserName = "Verificar el user name si es mayor a 6 caracteres y menor a 32.";
            log.error(errorTamanioUserName);
            return ResponseEntity.badRequest().build();
        }

        if(esPasswordErrado(request.getPassword(), 6)){
            log.error("Error with user password. Cannot create user. No password length <7.");
            return ResponseEntity.badRequest().build();
        }

        //Verficar password igual al confirPassword
        if (!request.getPassword().equals(request.getConfirmPassword())){
            log.error("No confirm password. Cannot create user.");
            return ResponseEntity.badRequest().build();
        }

        log.info("Usuario nombre a crear {}", request.getUserName());
        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setFirstName("Kevin");
        userRepository.save(user);
        log.info("User save! id: "+ user.getId());

        return ResponseEntity.ok(user);
    }
}
