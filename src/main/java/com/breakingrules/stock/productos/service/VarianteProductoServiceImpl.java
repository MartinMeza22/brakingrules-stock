package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.entity.Color;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    public List<VarianteProducto> listarTodas() {
        return varianteRepository.findAll();
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

        VarianteProducto existente = varianteRepository
                .findByProductoIdAndColorAndTalle(productoId, color, talle)
                .orElse(null);

        if (existente != null) {

            int stockActual = existente.getStock() != null ? existente.getStock() : 0;

            existente.setStock(stockActual + (stockInicial != null ? stockInicial : 0));

            return varianteRepository.save(existente);
        }

        VarianteProducto variante = new VarianteProducto();

        variante.setProducto(producto);
        variante.setTalle(talle);
        variante.setColor(color);

        if (stockInicial == null || stockInicial <= 0) {
            stockInicial = 0;
        }

        variante.setStock(stockInicial);

        variante.setCodigoBarras(
                generarCodigoBarras(producto.getSku(), color, talle)
        );

        return varianteRepository.save(variante);
    }
    @Override
    public void ingresarStock(Integer varianteId, Integer cantidad) {

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        int stockActual = variante.getStock() != null ? variante.getStock() : 0;

        variante.setStock(stockActual + cantidad);

        varianteRepository.save(variante);
    }

    @Override
    public void descontarStock(Integer varianteId, Integer cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("Cantidad inválida");
        }

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada"));

        int stockActual = variante.getStock() != null ? variante.getStock() : 0;

        // Permitimos stock negativo como pidió el cliente
        variante.setStock(stockActual - cantidad);
    }

    @Override
    public List<VarianteProducto> obtenerStockBajo(Integer limite) {

        if (limite == null) {
            limite = 5;
        }

        return varianteRepository.findByStockLessThanEqual(limite);
    }

    @Override
    public Integer obtenerStockTotal() {

        return varianteRepository.findAll()
                .stream()
                .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
                .sum();
    }

    @Override
    public void guardar(VarianteProducto variante) {

        validarDuplicado(
                variante.getProducto().getId(),
                variante.getColor(),
                variante.getTalle(),
                variante.getId()
        );

        if (variante.getPrecioPublicoFinal() == null ||
                variante.getPrecioPublicoFinal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Precio público inválido");
        }

        if (variante.getPrecioMayoristaFinal() == null ||
                variante.getPrecioMayoristaFinal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Precio mayorista inválido");
        }

        String nuevoCodigo = generarCodigoBarras(
                variante.getProducto().getSku(),
                variante.getColor(),
                variante.getTalle()
        );

        boolean existeCodigo = varianteRepository
                .existsByCodigoBarrasAndIdNot(nuevoCodigo, variante.getId());

        if (existeCodigo) {
            throw new RuntimeException("Ya existe una variante con ese código de barras");
        }

        variante.setCodigoBarras(nuevoCodigo);

        varianteRepository.save(variante);
    }

    @Override
    public void eliminar(Integer id) {
        varianteRepository.deleteById(id);
    }


    private String generarCodigoBarras(String sku, Color color, Talle talle) {
        return sku + "-" + color.name() + "-" + talle.name();
    }

    public void validarDuplicado(Integer productoId, Color color, Talle talle, Integer idActual) {

        VarianteProducto existente = varianteRepository
                .findByProductoIdAndColorAndTalle(productoId, color, talle)
                .orElse(null);

        if (existente != null && !existente.getId().equals(idActual)) {
            throw new RuntimeException("Ya existe una variante con ese color y talle");
        }
    }

    public List<Talle> obtenerTallesOcupados(Integer productoId, Color color, Integer varianteId) {

        return varianteRepository.findByProductoIdAndColor(productoId, color)
                .stream()
                .filter(v -> !v.getId().equals(varianteId)) // excluir actual
                .map(VarianteProducto::getTalle)
                .toList();
    }

    @Override
    public void sumarStock(Integer varianteId, Integer cantidad) {

        VarianteProducto variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        variante.setStock(variante.getStock() + cantidad);

        varianteRepository.save(variante);
    }
}