package com.breakingrules.stock.clientes.service;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.entity.TipoCliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    public List<Cliente> listarTodos() {
        return repository.findByActivoTrue();
    }

    @Override
    public Cliente obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    @Override
    public Cliente guardar(Cliente cliente) {

        cliente.setTipoCliente(TipoCliente.MAYORISTA);

        return repository.save(cliente);
    }

    @Override
    public Cliente actualizar(Integer id, Cliente cliente) {

        Cliente existente = obtenerPorId(id);

        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        existente.setDescripcion(cliente.getDescripcion());
        existente.setDocumento(cliente.getDocumento());
        existente.setCuil(cliente.getCuil());
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());
        existente.setDireccion(cliente.getDireccion());
        existente.setActivo(cliente.getActivo());
        existente.setTipoCliente(cliente.getTipoCliente());

        return repository.save(existente);
    }

    @Override
    public void eliminar(Integer id) {

        Cliente cliente = obtenerPorId(id);

        if(cliente.getTipoCliente() == TipoCliente.PUBLICO){
            throw new RuntimeException("No se puede eliminar el cliente público");
        }

        cliente.setActivo(false);

        repository.save(cliente);
    }

    @Override
    public Cliente obtenerClientePublico() {

        return repository.findByTipoCliente(TipoCliente.PUBLICO)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cliente público no encontrado"));
    }

    @Override
    public List<Cliente> obtenerMayoristas() {

        return repository.findByTipoCliente(TipoCliente.MAYORISTA);
    }
}