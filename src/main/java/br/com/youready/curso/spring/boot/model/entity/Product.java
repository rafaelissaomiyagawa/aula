package br.com.youready.curso.spring.boot.model.entity;

import br.com.youready.curso.spring.boot.exception.BusinessRuleException;
import br.com.youready.curso.spring.boot.model.dto.ProductResponse;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import br.com.youready.curso.spring.boot.validation.Sku;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "aula_product")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true, nullable = false)
  @NotEmpty
  @Sku
  private String sku;

  @Column(nullable = false)
  @NotEmpty
  private String name;

  @Column(nullable = false)
  @NotNull
  @DecimalMin("0.01")
  private BigDecimal price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @NotNull
  private ProductCategory category;

  @Column(nullable = false)
  @Min(0)
  private Integer stockQuantity;

  @Column(nullable = false)
  private boolean active = true;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    Product product = (Product) o;
    return getId() != null && Objects.equals(getId(), product.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }

  public void validateStockQuantity(Integer quantity) {
    if (this.getStockQuantity() < quantity) {
      throw new BusinessRuleException("Not enough stock for product: " + this.getName());
    }
  }

  public ProductResponse toProductResponse() {
    return new ProductResponse(
        this.getId(),
        this.getSku(),
        this.getName(),
        this.getPrice(),
        this.getCategory(),
        this.getStockQuantity(),
        this.isActive());
  }
}
