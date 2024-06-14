package com.tms;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class TaxiManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiManagementServiceApplication.class, args);
    }

}
