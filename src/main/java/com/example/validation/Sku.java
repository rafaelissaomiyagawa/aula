package com.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkuValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sku {
    String message() default "Invalid SKU format. Expected format is ABC-12345";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
