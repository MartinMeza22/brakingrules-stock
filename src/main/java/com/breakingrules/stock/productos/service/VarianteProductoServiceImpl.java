package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.entity.Color;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VarianteProductoServiceImpl implements VarianteProductoService {

    private final VarianteProductoRepository varianteRepository;
    private final ProductoRepository productoRepository;

    public VarianteProductoServiceImpl(
            VarianteProductoRepository varianteRepository,
            ProductoRepository productoRepository
    ) {
        this.varianteRepository = varianteRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public VarianteProducto obtenerPorId(Integer id) {
        return varianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
    }

    @Override
    public VarianteProducto crearVariante(
            Integer productoId,
            Talle talle,
            Color color,
            Integer stockInicial
    ) {

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        VarianteProducto variante = new VarianteProducto();
        variante.setProducto(producto);
        variante.setTalle(Talle.valueOf(String.valueOf(talle)));
        variante.setColor(color);
        variante.setStock(stockInicial != null ? stockInicial : 0);

        return varianteRepository.save(variante);
    }

    @Override
    public void ingresarStock(Integer varianteId, Integer cantidad) {

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        variante.setStock(variante.getStock() + cantidad);

        varianteRepository.save(variante);
    }

    @Override
    public void descontarStock(Integer varianteId, Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        // Permitimos stock negativo como pidió el cliente
        variante.setStock(variante.getStock() - cantidad);
    }

    @Override
    public List<VarianteProducto> obtenerStockBajo(Integer limite) {

        if (limite == null) {
            limite = 5;
        }

        Integer finalLimite = limite;
        return varianteRepository.findAll()
                .stream()
                .filter(v -> v.getStock() <= finalLimite)
                .toList();
    }

    @Override
    public Integer obtenerStockTotal() {

        return varianteRepository.findAll()
                .stream()
                .mapToInt(VarianteProducto::getStock)
                .sum();
    }

    @Override
    public void guardar(VarianteProducto variableProducto) {
        varianteRepository.save(variableProducto);
    }

    @Override
    public void eliminar(Integer id) {
        varianteRepository.deleteById(id);
    }
}