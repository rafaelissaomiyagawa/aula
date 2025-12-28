package br.com.youready.curso.spring.boot.aula.service;

import br.com.youready.curso.spring.boot.aula.Pessoa;
import br.com.youready.curso.spring.boot.aula.PessoaRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PessoaService {

  //    private final PessoaRepository pessoaRepository;

  private final List<PessoaRepository> repositoryList;

  public void save(Pessoa pessoa) {
    // Regras de negocio..

    //        pessoaRepository.save(pessoa);
    repositoryList.forEach((pessoaRepository -> pessoaRepository.save(pessoa)));
  }
}
