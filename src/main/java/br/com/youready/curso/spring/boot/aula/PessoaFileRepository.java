package br.com.youready.curso.spring.boot.aula;

import org.springframework.stereotype.Component;

@Component
public class PessoaFileRepository implements PessoaRepository {

  @Override
  public void save(Pessoa pessoa) {

    System.out.println("pessoa no arquivo = " + pessoa);
  }
}
