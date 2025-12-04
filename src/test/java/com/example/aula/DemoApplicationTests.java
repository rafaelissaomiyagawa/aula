package com.example.aula;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


// Teste de integracao
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    HomeController homeController;

	@Test
	void contextLoads() {
        homeController.index2();
	}

}
