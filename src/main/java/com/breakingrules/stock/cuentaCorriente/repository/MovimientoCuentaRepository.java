package com.breakingrules.stock.cuentaCorriente.repository;

import com.breakingrules.stock.cuentaCorriente.entity.MovimientoCuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoCuentaRepository extends JpaRepository<MovimientoCuenta, Integer> {
}
