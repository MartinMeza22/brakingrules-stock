package com.breakingrules.stock.productos.validation;

import com.breakingrules.stock.productos.entity.Producto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrecioValidoValidator implements ConstraintValidator<PrecioValido, Producto> {

    @Override
    public boolean isValid(Producto producto, ConstraintValidatorContext context) {

        if (producto.getPrecioVentaPublico() == null ||
                producto.getPrecioVentaMayorista() == null) {
            return true;
        }

        return producto.getPrecioVentaPublico()
                .compareTo(producto.getPrecioVentaMayorista()) >= 0;
    }
}