package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.caja.entity.MovimientoCaja;
import com.breakingrules.stock.caja.entity.TipoMovimiento;
import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;
import com.breakingrules.stock.cuentaCorriente.service.CuentaCorrienteService;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import com.breakingrules.stock.venta.dto.ItemVentaDTO;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.venta.entity.*;
import com.breakingrules.stock.caja.repository.MovimientoCajaRepository;
import com.breakingrules.stock.venta.repository.VentaDetalleRepository;
import com.breakingrules.stock.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository detalleRepository;
    private final ClienteRepository clienteRepository;
    private final VarianteProductoRepository varianteRepository;
    private final VarianteProductoService varianteService;

    public VentaServiceImpl(
            VentaRepository ventaRepository,
            VentaDetalleRepository detalleRepository,
            ClienteRepository clienteRepository,
            VarianteProductoRepository varianteRepository,
            VarianteProductoService varianteService
    ) {
        this.ventaRepository = ventaRepository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
        this.varianteRepository = varianteRepository;
        this.varianteService = varianteService;
    }

    @Override
    public Venta crearVenta(Integer clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setEstado(EstadoVenta.ABIERTA);
        venta.setTotal(BigDecimal.ZERO);

        return ventaRepository.save(venta);
    }

    @Override
    public void agregarProducto(Integer ventaId, Integer varianteId, Integer cantidad) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        BigDecimal precio = variante.getProducto().getPrecioVenta();

        VentaDetalle detalle = new VentaDetalle();
        detalle.setVenta(venta);
        detalle.setVariante(variante);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio);
        detalle.setSubtotal(precio.multiply(BigDecimal.valueOf(cantidad)));

        detalleRepository.save(detalle);

        BigDecimal nuevoTotal = venta.getTotal().add(detalle.getSubtotal());
        venta.setTotal(nuevoTotal);

        varianteService.descontarStock(varianteId, cantidad);
    }

    @Override
    public void finalizarVenta(Integer ventaId) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        venta.setEstado(EstadoVenta.FINALIZADA);
    }

    @Override
    public Venta obtenerVenta(Integer ventaId) {

        return ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
    }

    @Override
    public List<VentaDetalle> obtenerDetalles(Integer ventaId) {
        return detalleRepository.findByVentaId(ventaId);
    }

    @Override
    public List<Venta> listarVentas() {
        return ventaRepository.findAll();
    }
}