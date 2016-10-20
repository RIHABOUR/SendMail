package com.yuriikovalchuk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("spring-app.xml")
public class SendMailApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendMailApplication.class, args);
	}

}
