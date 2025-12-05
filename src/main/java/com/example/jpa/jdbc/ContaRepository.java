package com.example.jpa.jdbc;

import java.util.Optional;

public interface ContaRepository {
    Optional<Conta> findById(Long id);

    Conta save(Conta entity);
}
