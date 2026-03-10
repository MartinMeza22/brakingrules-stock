package com.breakingrules.stock.productos.repository;

import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    boolean existsBySku(String sku);
    List<Producto> findByActivoTrue();

    @Query("SELECT d.variante.producto.nombre FROM VentaDetalle d " +
            "GROUP BY d.variante.producto.id, d.variante.producto.nombre " +
            "ORDER BY SUM(d.cantidad) DESC")
    List<String> productosMasVendidos();

    @Query("SELECT SUM(d.cantidad) FROM VentaDetalle d " +
            "GROUP BY d.variante.producto.id ORDER BY SUM(d.cantidad) DESC")
    List<Integer> cantidadProductosVendidos();
}