package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.productos.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    BigDecimal totalVentasHoy();

    BigDecimal totalIngresosHoy();

    BigDecimal totalDeuda();

    BigDecimal totalAFavor();

    long clientesConDeuda();

    List<Producto> productosCriticos(Integer limite);
}
