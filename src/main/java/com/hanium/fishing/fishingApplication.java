package com.hanium.fishing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class fishingApplication {


	public static void main(String[] args) {
		SpringApplication.run(fishingApplication.class, args);
	}

}
