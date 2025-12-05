package com.example.jpa.jdbc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    List<PessoaSomenteComNome> findByNome(String nome);
}
