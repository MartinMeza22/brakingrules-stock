package com.breakingrules.stock.venta.repository;

import com.breakingrules.stock.venta.entity.EstadoVenta;
import com.breakingrules.stock.venta.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByEstado(EstadoVenta estado);

    List<Venta> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

}
