package br.com.youready.curso.spring.boot.repository;

import br.com.youready.curso.spring.boot.model.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByOrderNumber(String orderNumber);
}
