package br.com.youready.curso.spring.boot.service;

import br.com.youready.curso.spring.boot.exception.BusinessRuleException;
import br.com.youready.curso.spring.boot.model.dto.StockUpdate;
import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.publisher.InventoryEventPublisher;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;
  private final InventoryEventPublisher eventPublisher;

  public void updateStock(Long productId, Integer quantityChange) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(
                () -> new BusinessRuleException("Product not found with id: " + productId));

    int newStock = product.getStockQuantity() + quantityChange;
    if (newStock < 0) {
      throw new BusinessRuleException("Stock quantity cannot be negative.");
    }
    product.setStockQuantity(newStock);
    productRepository.save(product);
    eventPublisher.publishStockUpdated(new StockUpdate(product.getId(), newStock));
    log.info("Stock for product {} updated to {}.", product.getName(), newStock);
  }
}
