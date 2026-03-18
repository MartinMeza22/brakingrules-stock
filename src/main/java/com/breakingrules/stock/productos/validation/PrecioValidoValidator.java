package com.breakingrules.stock.productos.validation;

import com.breakingrules.stock.productos.entity.Producto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PrecioValidoValidator implements ConstraintValidator<PrecioValido, Producto> {

    @Override
    public boolean isValid(Producto producto, ConstraintValidatorContext context) {

        if (producto == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        boolean valido = true;

        valido &= validar(
                producto.getPrecioBasePublico(),
                producto.getPrecioBaseMayorista(),
                "precioBasePublico",
                "precioBaseMayorista",
                context
        );

        valido &= validar(
                producto.getPrecioEspecial1Publico(),
                producto.getPrecioEspecial1Mayorista(),
                "precioEspecial1Publico",
                "precioEspecial1Mayorista",
                context
        );

        valido &= validar(
                producto.getPrecioEspecial2Publico(),
                producto.getPrecioEspecial2Mayorista(),
                "precioEspecial2Publico",
                "precioEspecial2Mayorista",
                context
        );

        valido &= validar(
                producto.getPrecioEspecial3Publico(),
                producto.getPrecioEspecial3Mayorista(),
                "precioEspecial3Publico",
                "precioEspecial3Mayorista",
                context
        );

        return valido;
    }

    private boolean validar(BigDecimal publico,
                            BigDecimal mayorista,
                            String campoPublico,
                            String campoMayorista,
                            ConstraintValidatorContext context) {

        if ((publico != null && mayorista == null) ||
                (publico == null && mayorista != null)) {

            context.buildConstraintViolationWithTemplate(
                            "Debe completar ambos precios (público y mayorista)")
                    .addPropertyNode(campoPublico)
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "Debe completar ambos precios (público y mayorista)")
                    .addPropertyNode(campoMayorista)
                    .addConstraintViolation();

            return false;
        }

        if (publico == null) {
            return true;
        }

        if (publico.compareTo(mayorista) < 0) {

            context.buildConstraintViolationWithTemplate(
                            "El precio público no puede ser menor al mayorista")
                    .addPropertyNode(campoPublico)
                    .addConstraintViolation();

            context.buildConstraintViolationWithTemplate(
                            "El precio público no puede ser menor al mayorista")
                    .addPropertyNode(campoMayorista)
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}