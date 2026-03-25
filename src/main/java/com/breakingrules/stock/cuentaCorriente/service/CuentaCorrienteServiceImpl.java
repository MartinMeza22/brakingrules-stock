package com.breakingrules.stock.cuentaCorriente.service;

import com.breakingrules.stock.caja.service.CajaService;
import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.cuentaCorriente.entity.*;
import com.breakingrules.stock.cuentaCorriente.repository.CuentaCorrienteRepository;
import com.breakingrules.stock.cuentaCorriente.repository.MovimientoCuentaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CuentaCorrienteServiceImpl implements CuentaCorrienteService {

    private final CuentaCorrienteRepository cuentaCorrienteRepository;
    private final MovimientoCuentaRepository movimientoCuentaRepository;
    private final ClienteRepository clienteRepo;
    private final CajaService cajaService;

    @Override
    public List<CuentaCorriente> listarTodas() {
        return cuentaCorrienteRepository.findAll()
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public CuentaCorriente obtenerPorId(Integer id) {
        return cuentaCorrienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Override
    public void registrarPago(Integer clienteId, BigDecimal monto, String descripcion) {

        Cliente cliente = obtenerCliente(clienteId);
        CuentaCorriente cuenta = obtenerOCrearCuenta(cliente);

        MovimientoCuenta movimiento = MovimientoCuenta.builder()
                .cuentaCorriente(cuenta)
                .tipo(TipoMovimientoCuenta.PAGO)
                .origen(OrigenMovimiento.PAGO_MANUAL)
                .monto(monto)
                .descripcion(descripcion)
                .build();

        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));

        movimientoCuentaRepository.save(movimiento);
        cuentaCorrienteRepository.save(cuenta);

        String referencia = "Pago CC - " + cliente.getNombre();

        if (descripcion != null && !descripcion.isBlank()) {
            referencia += " | " + descripcion;
        }

        cajaService.registrarIngreso(monto, referencia);
    }

    private Cliente obtenerCliente(Integer id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public CuentaCorriente obtenerOCrearCuenta(Cliente cliente) {
        return cuentaCorrienteRepository.findByCliente(cliente)
                .orElseGet(() -> cuentaCorrienteRepository.save(
                        CuentaCorriente.builder()
                                .cliente(cliente)
                                .saldo(BigDecimal.ZERO)
                                .activo(true)
                                .build()
                ));
    }

    @Override
    @Transactional
    public void registrarDeuda(Integer clienteId, BigDecimal monto, String descripcion) {

        Cliente cliente = obtenerCliente(clienteId);
        CuentaCorriente cuenta = obtenerOCrearCuenta(cliente);

        MovimientoCuenta movimiento = MovimientoCuenta.builder()
                .cuentaCorriente(cuenta)
                .tipo(TipoMovimientoCuenta.DEUDA)
                .origen(OrigenMovimiento.VENTA)
                .monto(monto)
                .descripcion(descripcion)
                .build();

        cuenta.setSaldo(cuenta.getSaldo().add(monto));

        movimientoCuentaRepository.save(movimiento);
        cuentaCorrienteRepository.save(cuenta);
    }

}