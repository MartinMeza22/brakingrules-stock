package com.breakingrules.stock.dashboard.service;

import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.EstadoVenta;
import com.breakingrules.stock.venta.repository.VentaRepository;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService{

    private final VentaRepository ventaRepository;
    private final VarianteProductoRepository varianteRepository;
    private final ClienteRepository clienteRepository;

    public DashboardServiceImpl(
            VentaRepository ventaRepository,
            VarianteProductoRepository varianteRepository,
            ClienteRepository clienteRepository
    ) {
        this.ventaRepository = ventaRepository;
        this.varianteRepository = varianteRepository;
        this.clienteRepository = clienteRepository;
    }

    public BigDecimal ventasHoy() {

        LocalDateTime inicio = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime fin = LocalDateTime.now().with(LocalTime.MAX);

        return ventaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .filter(v -> v.getEstado() == EstadoVenta.FINALIZADA)
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal ventasMes() {

        LocalDateTime inicio = LocalDateTime.now()
                .withDayOfMonth(1)
                .with(LocalTime.MIN);

        LocalDateTime fin = LocalDateTime.now()
                .with(LocalTime.MAX);

        return ventaRepository.findByFechaBetween(inicio, fin)
                .stream()
                .filter(v -> v.getEstado() == EstadoVenta.FINALIZADA)
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long totalClientes() {
        return clienteRepository.count();
    }

    public long totalProductos() {
        return varianteRepository.count();
    }

    public Integer stockTotal() {

        return varianteRepository.findAll()
                .stream()
                .mapToInt(v -> v.getStock())
                .sum();
    }

    public List<Venta> ultimasVentas() {

        return ventaRepository.findAll()
                .stream()
                .filter(v -> v.getEstado() == EstadoVenta.FINALIZADA)
                .sorted((a,b) -> b.getFecha().compareTo(a.getFecha()))
                .limit(5)
                .toList();
    }
}