package com.breakingrules.stock.venta.repository;

import com.breakingrules.stock.venta.entity.EstadoVenta;
import com.breakingrules.stock.venta.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByEstado(EstadoVenta estado);

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Double totalVentas(@Param("inicio") LocalDateTime inicio,
                       @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin")
    Integer cantidadVentas(@Param("inicio") LocalDateTime inicio,
                           @Param("fin") LocalDateTime fin);

    @Query("""
           SELECT COALESCE(SUM(d.cantidad), 0)
           FROM VentaDetalle d
           WHERE d.venta.fecha BETWEEN :inicio AND :fin
           """)
    Integer productosVendidos(@Param("inicio") LocalDateTime inicio,
                              @Param("fin") LocalDateTime fin);

    @Query("""
           SELECT FUNCTION('to_char', v.fecha, 'YYYY-MM-DD')
           FROM Venta v
           WHERE v.fecha BETWEEN :inicio AND :fin
           ORDER BY v.fecha
           """)
    List<String> labelsVentas(@Param("inicio") LocalDateTime inicio,
                              @Param("fin") LocalDateTime fin);

    @Query("""
           SELECT COALESCE(SUM(v.total),0)
           FROM Venta v
           WHERE v.fecha BETWEEN :inicio AND :fin
           GROUP BY FUNCTION('to_char', v.fecha, 'YYYY-MM-DD')
           ORDER BY FUNCTION('to_char', v.fecha, 'YYYY-MM-DD')
           """)
    List<Double> datosVentas(@Param("inicio") LocalDateTime inicio,
                             @Param("fin") LocalDateTime fin);

    List<Venta> findAllByOrderByIdDesc();
}