package br.com.youready.curso.spring.boot.repository;

import br.com.youready.curso.spring.boot.model.dto.VipCustomerDTO;
import br.com.youready.curso.spring.boot.model.entity.Order;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByOrderNumber(String orderNumber);

  @Query(
      "SELECT new br.com.youready.curso.spring.boot.model.dto.VipCustomerDTO("
          + "o.customerEmail, COUNT(o), SUM(o.totalAmount), MAX(o.orderDate)) "
          + "FROM Order o "
          + "GROUP BY o.customerEmail "
          + "HAVING COUNT(o) >= :minOrders AND SUM(o.totalAmount) >= :minSpent")
  List<VipCustomerDTO> findVipCustomers(
      @Param("minOrders") Long minOrders, @Param("minSpent") BigDecimal minSpent);
}
