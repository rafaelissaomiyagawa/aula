package br.com.youready.curso.spring.boot.jpa.jdbc;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

  List<PessoaSomenteComNome> findByNome(String nome);
}
