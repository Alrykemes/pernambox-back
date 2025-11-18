package com.dev.pernambox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PernamboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(PernamboxApplication.class, args);
	}

}
