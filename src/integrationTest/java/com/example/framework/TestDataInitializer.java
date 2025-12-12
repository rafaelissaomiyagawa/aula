package com.example.framework;


import com.example.TestModelFactory;
import com.example.model.entity.Product;
import com.example.model.enums.ProductCategory;
import com.example.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;


public class TestDataInitializer {

    private final ProductRepository productRepository;

    public TestDataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Not a good practice! Just for example!
    public final static String LEGACY_CODE_BOOK_NAME = "Working With Legacy Code";
    public final static String REFACTORING_BOOK_NAME = "Refactoring";

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
