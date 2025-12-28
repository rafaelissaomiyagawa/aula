package br.com.youready.curso.spring.boot.jpa.jdbc;

import java.util.Optional;

public interface ContaRepository {
  Optional<Conta> findById(Long id);

  Conta save(Conta entity);
}
