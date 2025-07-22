package com.petland;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
public class PetlandApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetlandApplication.class, args);
	}

}
