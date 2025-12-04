package com.example.aula.service;

import com.example.aula.Pessoa;
import com.example.aula.PessoaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PessoaService {

//    private final PessoaRepository pessoaRepository;

    private final List<PessoaRepository> repositoryList;

    public void save(Pessoa pessoa) {
        // Regras de negocio..

//        pessoaRepository.save(pessoa);
        repositoryList.forEach(
                (pessoaRepository -> pessoaRepository.save(pessoa)));
    }
}
