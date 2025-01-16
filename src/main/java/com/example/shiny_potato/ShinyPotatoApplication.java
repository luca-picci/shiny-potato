package com.example.shiny_potato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;



@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ShinyPotatoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShinyPotatoApplication.class, args);
	}

}
