package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.dashboard.dto.DashboardDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.venta.entity.Venta;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    DashboardDTO obtenerEstadisticas(String filtro);
}
