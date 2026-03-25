package com.breakingrules.stock.venta.service;

import com.breakingrules.stock.caja.service.CajaService;
import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;
import com.breakingrules.stock.cuentaCorriente.entity.MovimientoCuenta;
import com.breakingrules.stock.cuentaCorriente.entity.OrigenMovimiento;
import com.breakingrules.stock.cuentaCorriente.entity.TipoMovimientoCuenta;
import com.breakingrules.stock.cuentaCorriente.repository.CuentaCorrienteRepository;
import com.breakingrules.stock.cuentaCorriente.repository.MovimientoCuentaRepository;
import com.breakingrules.stock.cuentaCorriente.service.CuentaCorrienteService;
import com.breakingrules.stock.cuentaCorriente.service.CuentaCorrienteServiceImpl;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.clientes.entity.TipoCliente;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import com.breakingrules.stock.venta.entity.*;
import com.breakingrules.stock.venta.repository.VentaDetalleRepository;
import com.breakingrules.stock.venta.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final VentaDetalleRepository detalleRepository;
    private final ClienteRepository clienteRepository;
    private final VarianteProductoRepository varianteRepository;
    private final VarianteProductoService varianteService;
    private final CuentaCorrienteService cuentaCorrienteService;
    private final CajaService cajaService;
    private final MovimientoCuentaRepository movimientoCuentaRepository;
    private final CuentaCorrienteRepository cuentaCorrienteRepository;


    public VentaServiceImpl(
            VentaRepository ventaRepository,
            VentaDetalleRepository detalleRepository,
            ClienteRepository clienteRepository,
            VarianteProductoRepository varianteRepository,
            VarianteProductoService varianteService,
            CuentaCorrienteService cuentaCorrienteService,
            CajaService cajaService,
            MovimientoCuentaRepository movimientoCuentaRepository,
            CuentaCorrienteRepository cuentaCorrienteRepository
    ) {
        this.ventaRepository = ventaRepository;
        this.detalleRepository = detalleRepository;
        this.clienteRepository = clienteRepository;
        this.varianteRepository = varianteRepository;
        this.varianteService = varianteService;
        this.cuentaCorrienteService = cuentaCorrienteService;
        this.cajaService = cajaService;
        this.movimientoCuentaRepository = movimientoCuentaRepository;
        this.cuentaCorrienteRepository = cuentaCorrienteRepository;
    }


    @Override
    public Venta crearVenta(Integer clienteId, String nombreCliente) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setFecha(LocalDateTime.now());
        venta.setEstado(EstadoVenta.ABIERTA);
        venta.setTotal(BigDecimal.ZERO);

        if(cliente.getTipoCliente() == TipoCliente.PUBLICO){

            if(nombreCliente == null || nombreCliente.isBlank()){
                nombreCliente = "Varios";
            }

            venta.setNombreClienteMostrador(nombreCliente);
        }

        return ventaRepository.save(venta);
    }


    @Override
    public void agregarProducto(Integer ventaId, Integer varianteId, Integer cantidad) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        Cliente cliente = venta.getCliente();
        Producto producto = variante.getProducto();

        BigDecimal precio;

        if(cliente.getTipoCliente() == TipoCliente.MAYORISTA){
            precio = producto.getPrecioBaseMayorista();
        }else{
            precio = producto.getPrecioBasePublico();
        }
        VentaDetalle detalle = new VentaDetalle();
        detalle.setVenta(venta);
        detalle.setVariante(variante);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio);
        detalle.setSubtotal(precio.multiply(BigDecimal.valueOf(cantidad)));

        detalle.setNombreProducto(variante.getProducto().getNombre());
        detalle.setSkuProducto(variante.getProducto().getSku());

        detalleRepository.save(detalle);

        BigDecimal nuevoTotal = venta.getTotal().add(detalle.getSubtotal());
        venta.setTotal(nuevoTotal);

        varianteService.descontarStock(varianteId, cantidad);
    }

    @Override
    public void finalizarVenta(Integer ventaId, BigDecimal descuento) {

        List<VentaDetalle> detalles = detalleRepository.findByVentaId(ventaId);

        if (detalles.isEmpty()) {
            throw new RuntimeException("No se puede finalizar una venta sin productos");
        }

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        BigDecimal total = venta.getTotal();

        if (descuento != null && descuento.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal descuentoMonto = total
                    .multiply(descuento)
                    .divide(BigDecimal.valueOf(100));

            total = total.subtract(descuentoMonto);

            venta.setDescuento(descuento);
        }

        venta.setTotal(total);
        venta.setEstado(EstadoVenta.FINALIZADA);

        Cliente cliente = venta.getCliente();

        String referencia = cliente.getTipoCliente() == TipoCliente.MAYORISTA
                ? "Venta MAYORISTA #" + venta.getId()
                : "Venta PUBLICO #" + venta.getId();
        cajaService.registrarIngreso(total, referencia);

        if (cliente.getTipoCliente() == TipoCliente.MAYORISTA) {

            cuentaCorrienteService.registrarDeuda(
                    cliente.getId(),
                    total,
                    "Venta #" + venta.getId()
            );
        }
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
        return ventaRepository.findAllByOrderByIdDesc();
    }

    @Override
    public void eliminarProducto(Integer detalleId) {

        VentaDetalle detalle = detalleRepository.findById(detalleId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        Venta venta = detalle.getVenta();

        varianteService.sumarStock(
                detalle.getVariante().getId(),
                detalle.getCantidad()
        );

        venta.getDetalles().remove(detalle);

        detalleRepository.delete(detalle);

        recalcularTotal(venta);

        ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public void reabrirVenta(Integer ventaId) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (venta.getEstado() != EstadoVenta.FINALIZADA) {
            return;
        }

        List<VentaDetalle> detalles = detalleRepository.findByVentaId(ventaId);

        for (VentaDetalle d : detalles) {
            varianteService.sumarStock(
                    d.getVariante().getId(),
                    d.getCantidad()
            );
        }

        cajaService.registrarEgreso(
                venta.getTotal(),
                "Reverso Venta #" + venta.getId()
        );

        Cliente cliente = venta.getCliente();

        if (cliente.getTipoCliente() == TipoCliente.MAYORISTA) {

            CuentaCorriente cuenta = cuentaCorrienteService.obtenerOCrearCuenta(cliente);

            MovimientoCuenta movimiento = MovimientoCuenta.builder()
                    .cuentaCorriente(cuenta)
                    .tipo(TipoMovimientoCuenta.PAGO)
                    .origen(OrigenMovimiento.VENTA)
                    .monto(venta.getTotal())
                    .descripcion("Reverso Venta #" + venta.getId())
                    .build();

            if (venta.getEstado() == EstadoVenta.ABIERTA) {
                throw new RuntimeException("La venta ya está abierta");
            }

            cuenta.setSaldo(cuenta.getSaldo().subtract(venta.getTotal()));

            movimientoCuentaRepository.save(movimiento);
            cuentaCorrienteRepository.save(cuenta);
        }

        venta.setEstado(EstadoVenta.ABIERTA);

        ventaRepository.save(venta);
    }

    private void recalcularTotal(Venta venta) {

        BigDecimal total = detalleRepository.findByVentaId(venta.getId())
                .stream()
                .map(VentaDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        venta.setTotal(total);
    }

    @Override
    @Transactional
    public void anularVenta(Integer ventaId) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (venta.getEstado() == EstadoVenta.ANULADA) {
            throw new RuntimeException("La venta ya está anulada");
        }

        List<VentaDetalle> detalles = detalleRepository.findByVentaId(ventaId);

        for (VentaDetalle d : detalles) {
            varianteService.sumarStock(
                    d.getVariante().getId(),
                    d.getCantidad()
            );
        }

        cajaService.registrarEgreso(
                venta.getTotal(),
                "Anulación Venta #" + venta.getId()
        );

        Cliente cliente = venta.getCliente();

        if (cliente.getTipoCliente() == TipoCliente.MAYORISTA) {

            CuentaCorriente cuenta = cuentaCorrienteService.obtenerOCrearCuenta(cliente);

            MovimientoCuenta movimiento = MovimientoCuenta.builder()
                    .cuentaCorriente(cuenta)
                    .tipo(TipoMovimientoCuenta.PAGO)
                    .origen(OrigenMovimiento.VENTA)
                    .monto(venta.getTotal())
                    .descripcion("Anulación Venta #" + venta.getId())
                    .build();

            cuenta.setSaldo(cuenta.getSaldo().subtract(venta.getTotal()));

            movimientoCuentaRepository.save(movimiento);
            cuentaCorrienteRepository.save(cuenta);
        }

        venta.setEstado(EstadoVenta.ANULADA);

        ventaRepository.save(venta);
    }

    @Transactional
    public void cancelarSiEstaVacia(Integer ventaId) {

        Venta venta = ventaRepository.findById(ventaId)
                .orElse(null);

        if (venta == null) return; // 🔥 evita el 500

        boolean sinProductos = venta.getDetalles() == null || venta.getDetalles().isEmpty();

        if (sinProductos && venta.getEstado() == EstadoVenta.ABIERTA) {
            venta.setEstado(EstadoVenta.ANULADA);
            ventaRepository.save(venta);
        }
    }
}