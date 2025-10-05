package com.code4fun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.code4fun"})
public class KeycloakIam3Application {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakIam3Application.class, args);
	}

}
