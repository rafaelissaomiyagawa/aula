package com.example.jpa.jdbc.infra;

import com.example.jpa.jdbc.Conta;
import com.example.jpa.jdbc.ContaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JPAContaRepository extends JpaRepository<Conta, Long>, ContaRepository {
}