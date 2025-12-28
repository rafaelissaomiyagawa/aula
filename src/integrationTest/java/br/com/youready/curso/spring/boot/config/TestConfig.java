package br.com.youready.curso.spring.boot.config;

import br.com.youready.curso.spring.boot.framework.TestDataInitializer;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

  @Bean
  public TestDataInitializer testDataInitializer(ProductRepository productRepository) {
    return new TestDataInitializer(productRepository);
  }
}
