package com.example.aula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class Pessoa {

    private final String nome;

    public static Pessoa comNome(String issao) {
        return new Pessoa(issao);
    }
}
