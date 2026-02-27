package com.breakingrules.stock.productos.repository;

import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByStockLessThan(Integer stock);
    List<Producto> findByStockGreaterThan(Integer stock);
    List<Producto> findByTalle(Talle talle);
    List<Producto> findByStockBetween(Integer min, Integer max);
}