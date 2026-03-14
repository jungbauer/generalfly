package com.jungbauer.generalfly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GeneralflyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeneralflyApplication.class, args);
	}

}
