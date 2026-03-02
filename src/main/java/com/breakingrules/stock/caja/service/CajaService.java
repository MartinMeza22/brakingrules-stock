package com.breakingrules.stock.caja.service;

import com.breakingrules.stock.caja.entity.MovimientoCaja;
import com.breakingrules.stock.caja.entity.TipoMovimiento;

import java.math.BigDecimal;
import java.util.List;

public interface CajaService {

    BigDecimal obtenerSaldoActual();
    void registrarMovimiento(TipoMovimiento tipo, BigDecimal monto, String referencia);
    List<MovimientoCaja> listarMovimientos();
}
