package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.caja.entity.MovimientoCaja;
import com.breakingrules.stock.caja.entity.TipoMovimiento;
import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;
import com.breakingrules.stock.cuentaCorriente.service.CuentaCorrienteService;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
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
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository ventaDetalleRepo;
    private final ProductoRepository productoRepo;
    private final ClienteRepository clienteRepo;
    private final MovimientoCajaRepository movimientoCajaRepo;
    private final CuentaCorrienteService cuentaCorrienteService;

    @Override
    @Transactional
    public void confirmarVenta(VentaDTO dto) {

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now());
        venta.setFormaPago(dto.getFormaPago());
        venta.setCliente(clienteRepo.findById(dto.getClienteId()).orElseThrow());
        ventaRepository.save(venta);

        BigDecimal total = BigDecimal.ZERO;

        for (ItemVentaDTO item : dto.getItems()) {
            Producto producto = productoRepo.findById(item.getProductoId())
                    .orElseThrow();


            VentaDetalle det = new VentaDetalle();
            det.setVenta(venta);
            det.setProducto(producto);
            det.setCantidad(item.getCantidad());
            det.setPrecioUnitario(producto.getPrecioVenta());

            BigDecimal subtotal = producto.getPrecioVenta()
                    .multiply(BigDecimal.valueOf(item.getCantidad()));

            det.setSubtotal(subtotal);
            ventaDetalleRepo.save(det);

        //    producto.setStock(producto.getStock() - item.getCantidad());
            total = total.add(subtotal);
        }

        venta.setTotal(total);

        BigDecimal pagado = dto.getMontoPagado() != null
                ? dto.getMontoPagado()
                : BigDecimal.ZERO;

        venta.setMontoPagado(pagado);

        BigDecimal vuelto = pagado.subtract(total);
        venta.setVuelto(vuelto.compareTo(BigDecimal.ZERO) > 0 ? vuelto : BigDecimal.ZERO);

        CuentaCorriente cuenta = cuentaCorrienteService.obtenerOCrearCuenta(venta.getCliente());
        BigDecimal saldoAFavor = cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0
                ? cuenta.getSaldo().abs()
                : BigDecimal.ZERO;

        BigDecimal efectivoDisponible = pagado.add(saldoAFavor);

        if (efectivoDisponible.compareTo(total) >= 0) {
            venta.setEstado(EstadoVenta.PAGADA);
        } else if (efectivoDisponible.compareTo(BigDecimal.ZERO) > 0) {
            venta.setEstado(EstadoVenta.PARCIAL);
        } else {
            venta.setEstado(EstadoVenta.PENDIENTE);
        }

        ventaRepository.save(venta);

        BigDecimal diferencia = total.subtract(pagado); // positivo = debe, negativo = a favor

        if (diferencia.compareTo(BigDecimal.ZERO) != 0) {
            if (diferencia.compareTo(BigDecimal.ZERO) > 0) {
                cuentaCorrienteService.registrarDeuda(
                        venta.getCliente().getId(),
                        diferencia,
                        "Venta #" + venta.getId()
                );
            } else {
                cuentaCorrienteService.registrarPago(
                        venta.getCliente().getId(),
                        diferencia.abs(),
                        "Pago extra Venta #" + venta.getId()
                );
            }
        }

        if (pagado.compareTo(BigDecimal.ZERO) > 0) {
            MovimientoCaja mov = new MovimientoCaja();
            mov.setFecha(LocalDateTime.now());
            mov.setTipo(TipoMovimiento.INGRESO);
            mov.setMonto(pagado);
            mov.setReferencia("Venta #" + venta.getId() + " - $" + diferencia);

            movimientoCajaRepo.save(mov);
        }
    }
 
    @Override
    public List<Cliente> obtenerClientes() {
        return clienteRepo.findAll();
    }

    @Override
    public List<Producto> obtenerProductos() {
        return productoRepo.findAll();
    }

    @Override
    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Venta> findById(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public List<VentaDetalle> obtenerDetallesVenta(Integer ventaId) {
        return ventaDetalleRepo.findByVentaId(ventaId);
    }

    public List<MovimientoCaja> listarMovimientos() {
        return movimientoCajaRepo.findAll();
    }

    @Transactional
    public Venta crearVenta(Venta venta) {

        Venta guardada = ventaRepository.save(venta);

        if (guardada.getCliente() != null) {

            BigDecimal total = guardada.getTotal();
            BigDecimal pagado = guardada.getMontoPagado() != null ? guardada.getMontoPagado() : BigDecimal.ZERO;

            BigDecimal diferencia = pagado.subtract(total);

            if (guardada.getFiado()) {
                cuentaCorrienteService.registrarDeuda(
                        guardada.getCliente().getId(),
                        total,
                        "Venta #" + guardada.getId()
                );
            }

            else if (diferencia.compareTo(BigDecimal.ZERO) > 0) {
                cuentaCorrienteService.registrarPago(
                        guardada.getCliente().getId(),
                        diferencia,
                        "Saldo a favor - Venta #" + guardada.getId()
                );
            }
        }

        return guardada;
    }
}