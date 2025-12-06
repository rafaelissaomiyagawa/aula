package com.example.junit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SkuValidator implements ConstraintValidator<Sku, String> {

    @Override
    public void initialize(Sku constraintAnnotation) {
    }

    @Override
    public boolean isValid(String skuField, ConstraintValidatorContext context) {
        if (skuField == null) {
            return true; // can be null, use @NotNull for that
        }
        // Example: ABC-12345
        return skuField.matches("[A-Z]{3}-\\d{5}");
    }
}
