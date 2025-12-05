package com.example.aula;

import com.example.jpa.jdbc.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


// Teste de integracao
@SpringBootTest
class DemoApplicationTests {

    @Autowired
    ContaService contaService;

    @Autowired
    ContaRepository contaRepository;

    // Pede pro applicationContext o objeto deste tipo!
    @Autowired
    PessoaFisicaRepository pessoaRepository;

    // JPA + Hibernate
	@Test
	void playground() {
        // ACID - É para não ferrar o banco de dados!
        contaService.transfere(-50, 102L, 152L);
    }

}
