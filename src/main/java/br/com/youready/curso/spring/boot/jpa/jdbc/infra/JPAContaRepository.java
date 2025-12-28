package br.com.youready.curso.spring.boot.jpa.jdbc.infra;

import br.com.youready.curso.spring.boot.jpa.jdbc.Conta;
import br.com.youready.curso.spring.boot.jpa.jdbc.ContaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JPAContaRepository extends JpaRepository<Conta, Long>, ContaRepository {}
