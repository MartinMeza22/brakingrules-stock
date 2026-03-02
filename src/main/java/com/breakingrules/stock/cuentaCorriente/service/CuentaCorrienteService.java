package com.breakingrules.stock.cuentaCorriente.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaCorrienteService {

    List<CuentaCorriente> listarTodas();

    CuentaCorriente obtenerPorId(Integer id);

    void registrarDeuda(Integer clienteId, BigDecimal monto, String descripcion);

    void registrarPago(Integer clienteId, BigDecimal monto, String descripcion);

    CuentaCorriente obtenerOCrearCuenta(Cliente cliente);
}