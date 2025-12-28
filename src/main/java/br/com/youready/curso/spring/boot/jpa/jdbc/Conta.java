package br.com.youready.curso.spring.boot.jpa.jdbc;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUL_CONTA")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Conta {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(nullable = false)
  private Long id;

  private String nome;

  private Integer valor;

  private String tipoConta;

  public Conta(String nome, Integer valor) {
    this.valor = valor;
    this.nome = nome;
  }

  public static Conta comNome(String nome) {
    return new Conta(nome, 100000);
  }

  public void debita(Integer valor) {
    this.valor -= valor;
  }

  public void credita(Integer valor) {
    this.valor += valor;
  }
}
