package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.caja.entity.MovimientoCaja;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VentaService {

    Venta crearVenta(Integer clienteId);

    void agregarProducto(Integer ventaId, Integer varianteId, Integer cantidad);

    void finalizarVenta(Integer ventaId, BigDecimal descuento);

    Venta obtenerVenta(Integer ventaId);

    List<VentaDetalle> obtenerDetalles(Integer ventaId);

    List<Venta> listarVentas();
}