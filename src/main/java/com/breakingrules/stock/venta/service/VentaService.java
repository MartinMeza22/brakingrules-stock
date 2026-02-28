package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;

import java.util.List;
import java.util.Optional;

public interface VentaService {

    void confirmarVenta(VentaDTO dto);

    List<Cliente> obtenerClientes();

    List<Venta> obtenerVentas();

    List<Producto> obtenerProductos();

    Optional<Venta> findById(Integer id);

    List<VentaDetalle> obtenerDetallesVenta(Integer ventaId);
}
