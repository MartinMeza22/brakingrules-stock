package com.breakingrules.stock.clientes.repository;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.entity.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByDocumento(String documento);
    List<Cliente> findByTipoCliente(TipoCliente tipoCliente);
    List<Cliente> findByActivoTrue();
    List<Cliente> findByTipoClienteAndActivoTrue(TipoCliente tipoCliente);
}