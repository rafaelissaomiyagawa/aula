package br.com.youready.curso.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Config by Convention
@SpringBootApplication
public class CursoSpringBoot {

  // JPA - Java Persistence API (Interface) - Jakarta
  // Hibernate
  public static void main(String[] args) {
    SpringApplication.run(CursoSpringBoot.class, args);
  }
}
