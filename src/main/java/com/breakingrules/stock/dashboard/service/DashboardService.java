package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.productos.entity.Producto;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    BigDecimal obtenerVentasDelDia();

    long cantidadVentasDelDia();

    BigDecimal obtenerIngresosDelDia();

    BigDecimal obtenerDeudaTotal();

    BigDecimal obtenerSaldoAFavorTotal();

    List<ProductoCantidadDTO> topProductosVendidos(Integer top);

    BigDecimal totalVentasHoy();

    BigDecimal totalIngresosHoy();

    BigDecimal totalDeuda();

    BigDecimal totalAFavor();

    long clientesConDeuda();

    List<Producto> productosCriticos(Integer limite);

    BigDecimal totalVentasAyer();
}
