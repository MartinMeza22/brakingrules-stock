package com.breakingrules.stock.productos.repository;

import com.breakingrules.stock.productos.entity.Color;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Integer> {
    boolean existsByProductoIdAndColorAndTalle(
            Integer productoId,
            Color color,
            Talle talle
    );
}
