package com.breakingrules.stock.config;

import com.breakingrules.stock.clientes.entity.Cliente;
import com.breakingrules.stock.clientes.entity.TipoCliente;
import com.breakingrules.stock.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    @Override
    public void run(String... args) {

        boolean existe = clienteRepository.existsByDocumento("0");

        if(!existe){

            Cliente cliente = new Cliente();

            cliente.setNombre("Consumidor");
            cliente.setApellido("Final");
            cliente.setDocumento("0");
            cliente.setTipoCliente(TipoCliente.PUBLICO);
            cliente.setTieneCuentaCorriente(false);
            cliente.setActivo(true);

            clienteRepository.save(cliente);
        }
    }
}