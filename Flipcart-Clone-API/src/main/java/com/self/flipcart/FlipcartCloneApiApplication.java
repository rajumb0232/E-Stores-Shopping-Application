package com.self.flipcart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FlipcartCloneApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlipcartCloneApiApplication.class, args);
	}

}
