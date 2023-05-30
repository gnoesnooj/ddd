package com.ddd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class DddApplication {

	public static void main(String[] args) {
		SpringApplication.run(DddApplication.class, args);
		System.out.println("hello");
	}

}
