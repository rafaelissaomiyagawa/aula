package br.com.youready.curso.spring.boot.aula;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PessoaPostgresRepository implements PessoaRepository {

  @Override
  public void save(Pessoa pessoa) {
    System.out.println("pessoa no postgres = " + pessoa);
  }
}
