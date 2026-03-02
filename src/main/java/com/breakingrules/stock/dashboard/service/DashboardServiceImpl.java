package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;
import com.breakingrules.stock.cuentaCorriente.repository.CuentaCorrienteRepository;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final VentaRepository ventaRepo;
    private final CuentaCorrienteRepository cuentaRepo;
    private final ProductoRepository productoRepo;

    public BigDecimal totalVentasHoy() {
        LocalDate hoy = LocalDate.now();
        return ventaRepo.findAll().stream()
                .filter(v -> v.getFecha().toLocalDate().isEqual(hoy))
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalIngresosHoy() {
        LocalDate hoy = LocalDate.now();
        return ventaRepo.findAll().stream()
                .filter(v -> v.getFecha().toLocalDate().isEqual(hoy))
                .map(Venta::getMontoPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalDeuda() {
        return cuentaRepo.findAll().stream()
                .map(CuentaCorriente::getSaldo)
                .filter(s -> s.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal totalAFavor() {
        return cuentaRepo.findAll().stream()
                .map(CuentaCorriente::getSaldo)
                .filter(s -> s.compareTo(BigDecimal.ZERO) < 0)
                .map(BigDecimal::abs)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long clientesConDeuda() {
        return cuentaRepo.findAll().stream()
                .filter(c -> c.getSaldo().compareTo(BigDecimal.ZERO) > 0)
                .count();
    }

    public List<Producto> productosCriticos(Integer limite) {
        return productoRepo.findAll().stream()
                .filter(p -> p.getStock() <= limite)
                .toList();
    }

}