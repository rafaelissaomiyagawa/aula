package br.com.youready.curso.spring.boot.jpa.jdbc.infra;

import br.com.youready.curso.spring.boot.jpa.jdbc.Conta;
import br.com.youready.curso.spring.boot.jpa.jdbc.ContaRepository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository("jdbc")
public class JDBCTemplateContaRepository implements ContaRepository {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Override
  public Optional<Conta> findById(Long id) {
    String sql = "SELECT id, valor FROM aul_conta WHERE id = ?";

    try {
      Conta conta = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Conta.class), id);
      return Optional.ofNullable(conta);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Conta save(Conta conta) {
    if (conta.getId() == null) {
      // INSERT - conta nova
      return insert(conta);
    } else {
      // UPDATE - conta existente
      return update(conta);
    }
  }

  private Conta insert(Conta conta) {
    String sql = "INSERT INTO aul_conta (valor) VALUES (?)";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(
        connection -> {
          PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          ps.setInt(1, conta.getValor());
          return ps;
        },
        keyHolder);

    Long idGerado = Objects.requireNonNull(keyHolder.getKey()).longValue();
    conta.setId(idGerado);

    return conta;
  }

  private Conta update(Conta conta) {
    String sql = "UPDATE aul_conta SET valor = ? WHERE id = ?";

    int rowsAffected = jdbcTemplate.update(sql, conta.getValor(), conta.getId());

    if (rowsAffected == 0) {
      throw new RuntimeException("Conta não encontrada para atualização: " + conta.getId());
    }

    return conta;
  }
}
