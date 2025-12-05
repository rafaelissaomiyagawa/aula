package com.example.jpa.jdbc;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContaService {

    private final ContaRepository contaRepository;

    // Injecao por construtor
    public ContaService(@Qualifier("jdbc") ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    // ACID!
    public void transfere(Integer valor, Long contaDe, Long contaPara) {
        System.out.println("Usando a impl: " + contaRepository.getClass().getSimpleName());
        Conta contaSaida = contaRepository.findById(contaDe)
                                     .orElseThrow(EntityNotFoundException::new);


        Conta contaEntrada = contaRepository.findById(contaPara).orElseThrow(EntityNotFoundException::new);

        contaSaida.debita(valor);
        contaRepository.save(contaSaida);

        if (valor < 0) {
            throw new RuntimeException("pode nao!");
        }

        contaEntrada.credita(valor);
        contaRepository.save(contaEntrada);
    }
}
