package br.com.youready.curso.spring.boot.framework;

import br.com.youready.curso.spring.boot.TestModelFactory;
import br.com.youready.curso.spring.boot.model.entity.Product;
import br.com.youready.curso.spring.boot.model.enums.ProductCategory;
import br.com.youready.curso.spring.boot.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

public class TestDataInitializer {

  private final ProductRepository productRepository;

  public TestDataInitializer(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  // Not a good practice! Just for example!
  public static final String LEGACY_CODE_BOOK_NAME = "Working With Legacy Code";
  public static final String REFACTORING_BOOK_NAME = "Refactoring";

  @Transactional
  public void initializeDBWithDefaultProducts() {
    Product workingWithLegacyBook = TestModelFactory.createProduct();
    workingWithLegacyBook.setName(LEGACY_CODE_BOOK_NAME);
    workingWithLegacyBook.setCategory(ProductCategory.BOOKS);

    Product refactoringBook = TestModelFactory.createProduct();
    refactoringBook.setName(REFACTORING_BOOK_NAME);
    refactoringBook.setCategory(ProductCategory.BOOKS);

    productRepository.save(workingWithLegacyBook);
    productRepository.save(refactoringBook);
  }
}
