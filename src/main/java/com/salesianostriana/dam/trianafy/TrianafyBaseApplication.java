package com.salesianostriana.dam.trianafy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info =
@Info(description = "Una API de las playlist de Triana",
        version = "1.0",
        contact = @Contact(email = "arturo200212@gmail.com", name = "Arturo Céspedes Pedrazas"),
        title = "TRIANAFY"
)
)
public class TrianafyBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrianafyBaseApplication.class, args);
    }

}
