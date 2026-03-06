package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.venta.entity.Venta;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    BigDecimal ventasHoy();

    BigDecimal ventasMes();

    long totalClientes();

    long totalProductos();

    Integer stockTotal();

    List<Venta> ultimasVentas();
}
