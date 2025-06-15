package com.example.bioweatherbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class BioweatherBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BioweatherBackendApplication.class, args);
	}

}
