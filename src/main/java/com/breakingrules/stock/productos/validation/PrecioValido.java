package com.breakingrules.stock.productos.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PrecioValidoValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrecioValido {

    String message() default "El precio público no puede ser menor al mayorista";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}