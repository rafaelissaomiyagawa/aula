package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Config by Convention
@SpringBootApplication
public class DemoApplication {

    // JPA - Java Persistence API (Interface) - Jakarta
    // Hibernate
	public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
