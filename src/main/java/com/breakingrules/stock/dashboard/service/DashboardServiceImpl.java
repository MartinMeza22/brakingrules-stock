package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;
import com.breakingrules.stock.cuentaCorriente.service.CuentaCorrienteService;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;
import com.breakingrules.stock.venta.repository.VentaDetalleRepository;
import com.breakingrules.stock.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepository;
    private final CuentaCorrienteService cuentaCorrienteService;


    @Override
    public BigDecimal obtenerVentasDelDia() {
        LocalDate hoy = LocalDate.now();
        return ventaRepository.findAll()
                .stream()
                .filter(v -> v.getFecha().toLocalDate().isEqual(hoy))
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long cantidadVentasDelDia() {
        LocalDate hoy = LocalDate.now();
        return ventaRepository.findAll()
                .stream()
                .filter(v -> v.getFecha().toLocalDate().isEqual(hoy))
                .count();
    }

    @Override
    public BigDecimal obtenerIngresosDelDia() {
        LocalDate hoy = LocalDate.now();
        return ventaRepository.findAll()
                .stream()
                .filter(v -> v.getFecha().toLocalDate().isEqual(hoy))
                .map(Venta::getMontoPagado)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal obtenerDeudaTotal() {
        return cuentaCorrienteService.listarTodas()
                .stream()
                .map(CuentaCorriente::getSaldo)
                .filter(s -> s.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal obtenerSaldoAFavorTotal() {
        return cuentaCorrienteService.listarTodas()
                .stream()
                .map(CuentaCorriente::getSaldo)
                .filter(s -> s.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public long clientesConDeuda() {
        return cuentaCorrienteService.listarTodas()
                .stream()
                .map(CuentaCorriente::getSaldo)
                .filter(s -> s.compareTo(BigDecimal.ZERO) > 0)
                .count();
    }

    @Override
    public List<ProductoCantidadDTO> topProductosVendidos(Integer top) {

        Map<Producto, Integer> cantidades = new HashMap<>();

        for (VentaDetalle det : ventaDetalleRepository.findAll()) {
            cantidades.merge(det.getProducto(), det.getCantidad(), Integer::sum);
        }

        return cantidades.entrySet()
                .stream()
                .sorted(Map.Entry.<Producto, Integer>comparingByValue().reversed())
                .limit(top)
                .map(e -> new ProductoCantidadDTO(
                        e.getKey().getNombre(),
                        e.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> productosCriticos(Integer limite) {
        return ventaDetalleRepository.findAll()
                .stream()
                .map(VentaDetalle::getProducto)
                .distinct()
                .filter(p -> p.getStock() <= limite)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal totalVentasHoy() {
        return obtenerVentasDelDia();
    }

    @Override
    public BigDecimal totalIngresosHoy() {
        return obtenerIngresosDelDia();
    }

    @Override
    public BigDecimal totalDeuda() {
        return obtenerDeudaTotal();
    }

    @Override
    public BigDecimal totalAFavor() {
        return obtenerSaldoAFavorTotal();
    }

    @Override
    public BigDecimal totalVentasAyer() {
        LocalDate ayer = LocalDate.now().minusDays(1);
        return ventaRepository.findAll()
                .stream()
                .filter(v -> v.getFecha().toLocalDate().isEqual(ayer))
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}