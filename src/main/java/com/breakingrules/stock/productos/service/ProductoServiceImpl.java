package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.dto.ProductoStatsDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.TipoTalle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.nio.file.*;

@Service
public class ProductoServiceImpl implements ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository repository;
    private static final int STOCK_BAJO_LIMITE = 5;

    public ProductoServiceImpl(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<ProductoDTO> listarTodosSinPaginacion() {

        log.info("Listando productos activos");

        List<ProductoDTO> productos = repository.findByActivoTrue()
                .stream()
                .map(this::toDTO)
                .toList();

        log.info("Total productos obtenidos: {}", productos.size());

        return productos;
    }

    public Producto guardar(Producto producto) {

        log.info("Intentando guardar producto: {}", producto != null ? producto.getNombre() : "null");

        validarProducto(producto);

        if (producto.getTipoTalle() == TipoTalle.NUMERICO) {

            log.info("Producto NUMERICO detectado → limpiando precios especiales");

            producto.setPrecioEspecial1Publico(null);
            producto.setPrecioEspecial1Mayorista(null);
            producto.setPrecioEspecial2Publico(null);
            producto.setPrecioEspecial2Mayorista(null);
            producto.setPrecioEspecial3Publico(null);
            producto.setPrecioEspecial3Mayorista(null);

        }

        if (producto.getTipoTalle() == null) {
            log.warn("TipoTalle null → default ALFABETICO");
            producto.setTipoTalle(TipoTalle.ALFABETICO);
        }

        Producto guardado = repository.save(producto);

        log.info("Producto guardado correctamente con ID: {}", guardado.getId());

        return guardado;
    }

    public Producto actualizar(Producto producto) {
        log.info("Actualizando producto ID: {}", producto.getId());

        Producto guardado = repository.save(producto);

        log.info("Producto actualizado correctamente: {}", guardado.getId());
        return guardado;
    }

    public void eliminar(Integer id) {

        log.info("Intentando desactivar producto con ID: {}", id);

        if (id == null) {
            log.warn("Intento de eliminación con ID null");
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Producto producto = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe"));

        producto.setActivo(false);

        if (producto.getVariantes() != null) {
            producto.getVariantes().forEach(variante -> variante.setActivo(false));
        }

        repository.save(producto);

        log.info("Producto y variantes desactivados correctamente. ID: {}", id);
    }

    private void validarProducto(Producto producto) {

        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }

        if (producto.getPrecioBasePublico() == null ||
                producto.getPrecioBasePublico().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio público base debe ser mayor a 0");
        }

        if (producto.getPrecioBaseMayorista() == null ||
                producto.getPrecioBaseMayorista().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio mayorista base debe ser mayor a 0");
        }

        if (producto.getTipoTalle() == TipoTalle.ALFABETICO) {

            if (producto.getPrecioEspecial1Publico() != null &&
                    producto.getPrecioEspecial1Mayorista() != null &&
                    producto.getPrecioEspecial1Publico().compareTo(producto.getPrecioEspecial1Mayorista()) < 0) {
                throw new IllegalArgumentException("Error en precios 2XL/3XL");
            }

        }

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
    }

    public List<ProductoDTO> buscarPorNombre(String nombre) {
        log.info("Buscando productos por nombre: {}", nombre);

        if (nombre == null || nombre.isBlank()) {
            log.warn("Búsqueda fallida: nombre vacío");
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        List<ProductoDTO> resultados = repository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();

        log.info("Resultados encontrados: {}", resultados.size());
        return resultados;
    }

    public Producto obtenerEntidadPorId(Integer id) {
        log.info("Buscando producto por ID: {}", id);

        if (id == null) {
            log.warn("Búsqueda fallida: ID null");
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Producto producto = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado. ID: {}", id);
                    return new IllegalArgumentException("Producto no encontrado");
                });

        log.info("Producto encontrado: {}", producto.getNombre());
        return producto;
    }

    public String exportarCSV() {
        log.info("Iniciando exportación de productos a CSV");

        try {
            List<Producto> productos = repository.findAll();
            log.info("Cantidad de productos a exportar: {}", productos.size());

            StringBuilder csv = new StringBuilder();

            csv.append("ID,SKU,Nombre,Costo,PrecioBasePublico,PrecioBaseMayorista,Activo\n");

            for (Producto p : productos) {

                csv.append(p.getId()).append(",")
                        .append(p.getSku()).append(",")
                        .append(p.getNombre()).append(",")
                        .append(p.getCosto()).append(",")
                        .append(p.getPrecioBasePublico()).append(",")
                        .append(p.getPrecioBaseMayorista()).append(",")
                        .append(p.getActivo())
                        .append("\n");
            }

            log.info("Exportación CSV completada correctamente");
            return csv.toString();

        } catch (Exception e) {
            log.error("Error al exportar productos a CSV", e);
            throw e;
        }
    }

    public Page<ProductoDTO> listarPaginado(int page, int size) {

        log.info("Listando productos paginados - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<Producto> productos = repository.findAll(pageable);

        log.info("Total elementos encontrados: {}", productos.getTotalElements());

        return productos.map(this::toDTO);
    }

    private ProductoDTO toDTO(Producto p) {
        return new ProductoDTO(
                p.getId(),
                p.getSku(),
                p.getNombre(),
                p.getCosto(),
                p.getPrecioBasePublico(),
                p.getPrecioBaseMayorista(),
                p.getPrecioEspecial1Publico(),
                p.getPrecioEspecial2Publico(),
                p.getPrecioEspecial3Publico(),
                p.getPrecioEspecial1Mayorista(),
                p.getPrecioEspecial2Mayorista(),
                p.getPrecioEspecial3Mayorista(),
                p.getTipoTalle(),
                p.getActivo(),
                p.getStockTotal(),
                p.getProveedor() != null
                        ? p.getProveedor().getNombre()
                        : "Sin proveedor"
        );
    }

    @Override
    public boolean existeSku(String sku) {
        return repository.existsBySku(sku);
    }


    @Override
    public List<VarianteProducto> obtenerVariantesOrdenadas(Integer productoId) {

        Producto producto = obtenerEntidadPorId(productoId);

        List<VarianteProducto> variantes = new ArrayList<>(producto.getVariantes());

        variantes.sort(
                Comparator
                        .comparing((VarianteProducto v) -> v.getColor().ordinal())
                        .thenComparing(v -> v.getTalle().ordinal())
        );

        return variantes;
    }

    public Integer obtenerStockTotal(Integer productoId) {

        Producto producto = obtenerEntidadPorId(productoId);

        return producto.getVariantes()
                .stream()
                .mapToInt(v -> v.getStock() != null ? v.getStock() : 0)
                .sum();
    }

}
