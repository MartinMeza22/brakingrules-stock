package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.dashboard.dto.DashboardDTO;
import com.breakingrules.stock.dashboard.service.DashboardService;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import com.breakingrules.stock.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    public DashboardDTO obtenerEstadisticas(String filtro){

        LocalDate inicio;
        LocalDate fin = LocalDate.now();

        switch (filtro){

            case "DIARIO":
                inicio = LocalDate.now();
                break;

            case "SEMANAL":
                inicio = LocalDate.now().minusDays(7);
                break;

            case "ANUAL":
                inicio = LocalDate.now().minusYears(1);
                break;

            default:
                inicio = LocalDate.now().minusMonths(1);
        }

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

        List<VarianteProducto> productosStockBajoLista = varianteProductoRepository.productosStockBajo(10);

        Double totalVentas = ventaRepository.totalVentas(inicioDateTime, finDateTime);
        Integer cantidadVentas = ventaRepository.cantidadVentas(inicioDateTime, finDateTime);

        Double ticketPromedio = cantidadVentas == 0 ? 0 : totalVentas / cantidadVentas;

        Integer productosVendidos = ventaRepository.productosVendidos(inicioDateTime, finDateTime);

        Integer clientesActivos = Math.toIntExact(clienteRepository.count());

        Integer stockBajo = varianteProductoRepository.stockBajo(10);

        List<String> labelsVentas = ventaRepository.labelsVentas(inicioDateTime, finDateTime);
        List<Double> datosVentas = ventaRepository.datosVentas(inicioDateTime, finDateTime);

        List<String> labelsProductos = productoRepository.productosMasVendidos();
        List<Integer> datosProductos = productoRepository.cantidadProductosVendidos();

        return DashboardDTO.builder()
                .totalVentas(totalVentas)
                .cantidadVentas(cantidadVentas)
                .ticketPromedio(ticketPromedio)
                .productosVendidos(productosVendidos)
                .clientesActivos(clientesActivos)
                .productosStockBajo(stockBajo)
                .labelsVentas(labelsVentas)
                .datosVentas(datosVentas)
                .labelsProductos(labelsProductos)
                .datosProductos(datosProductos)
                .productosStockBajoLista(productosStockBajoLista)
                .build();
    }
}