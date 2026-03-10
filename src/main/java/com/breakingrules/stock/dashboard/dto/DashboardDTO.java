package com.breakingrules.stock.dashboard.dto;

import com.breakingrules.stock.productos.entity.VarianteProducto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashboardDTO {

    private Double totalVentas;

    private Integer cantidadVentas;

    private Double ticketPromedio;

    private Integer productosVendidos;

    private Integer clientesActivos;

    private Integer productosStockBajo;

    private List<String> labelsVentas;

    private List<Double> datosVentas;

    private List<String> labelsProductos;

    private List<Integer> datosProductos;

    private List<VarianteProducto> productosStockBajoLista;
}