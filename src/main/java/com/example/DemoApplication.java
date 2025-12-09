package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.kafka.annotation.EnableKafka;

// Config by Convention
@SpringBootApplication
@EnableKafka
public class DemoApplication {

    // JPA - Java Persistence API (Interface) - Jakarta
    // Hibernate
	public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
