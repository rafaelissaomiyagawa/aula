package com.example.aula;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;


@ToString
@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class Pessoa {

    private final String nome;

    public static Pessoa comNome(String issao) {
        return new Pessoa(issao);
    }
}
