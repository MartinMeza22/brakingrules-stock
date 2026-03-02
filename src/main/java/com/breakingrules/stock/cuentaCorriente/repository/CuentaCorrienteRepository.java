package com.breakingrules.stock.cuentaCorriente.repository;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.cuentaCorriente.entity.CuentaCorriente;
import com.breakingrules.stock.productos.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaCorrienteRepository extends JpaRepository<CuentaCorriente, Integer> {
    Optional<CuentaCorriente> findByCliente(Cliente cliente);
    Optional<CuentaCorriente> findByClienteId(Integer id);
}
