package br.com.youready.curso.spring.boot;

import static org.instancio.Select.field;

import br.com.youready.curso.spring.boot.model.dto.OrderItemRequest;
import br.com.youready.curso.spring.boot.model.dto.OrderRequest;
import br.com.youready.curso.spring.boot.model.entity.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import org.instancio.Instancio;
import org.instancio.Model;

public class TestModelFactory {

  public static final Model<Product> PRODUCT_MODEL =
      Instancio.of(Product.class)
          .generate(field(Product::getSku), gen -> gen.text().pattern("#C#C#C-#d#d#d#d#d"))
          .generate(
              field(Product::getPrice),
              gen -> gen.math().bigDecimal().min(new BigDecimal("10")).max(new BigDecimal("100")))
          .generate(field(Product::getStockQuantity), gen -> gen.ints().min(1).max(10))
          .set(field(Product::isActive), true)
          .ignore(field(Product::getId))
          .toModel();

  public static final Model<OrderRequest> ORDER_REQUEST_MODEL =
      Instancio.of(OrderRequest.class)
          .generate(field(OrderRequest::customerEmail), gen -> gen.net().email())
          .toModel();

  public static final Model<OrderItemRequest> ORDER_ITEM_REQUEST_MODEL =
      Instancio.of(OrderItemRequest.class).toModel();

  public static Product createProduct() {
    return Instancio.create(PRODUCT_MODEL);
  }

  public static OrderRequest createOrderRequest(OrderItemRequest... orderItemRequest) {
    return Instancio.of(ORDER_REQUEST_MODEL)
        .set(field(OrderRequest::items), Arrays.asList(orderItemRequest))
        .create();
  }

  public static OrderItemRequest createOrderItemRequest(Product product) {
    return Instancio.of(ORDER_ITEM_REQUEST_MODEL)
        .set(field(OrderItemRequest::productId), product.getId())
        .generate(
            field(OrderItemRequest::quantity),
            gen -> gen.ints().range(1, product.getStockQuantity()))
        .create();
  }
}
