package br.com.youready.curso.spring.boot.aula;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

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
