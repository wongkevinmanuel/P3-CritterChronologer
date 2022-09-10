package com.udacity.jdnd.course3.critter.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "Pet REST API",
                "This is a CRUD of pet.",
                "1.0",
                "http://www.udacity.com/tos",
                new Contact("Kevin Wong","https://www.linkedin.com/in/kevin-wong-b6266510b/", null),
                "License of API", "http://www.udacity.com/license", Collections.emptyList());
    }
}
