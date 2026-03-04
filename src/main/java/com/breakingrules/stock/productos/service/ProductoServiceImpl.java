package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.dto.ProductoStatsDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
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
import java.util.List;
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
        log.info("Listando todos los productos");

        List<ProductoDTO> productos = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        log.info("Total productos obtenidos: {}", productos.size());
        return productos;
    }

    public Producto guardar(Producto producto) {
        log.info("Intentando guardar producto: {}", producto != null ? producto.getNombre() : "null");

        validarProducto(producto);

        // Generar código de barras si no tiene
        if (producto.getCodigoBarras() == null || producto.getCodigoBarras().isBlank()) {
            String codigo = generarCodigoBarrasParaProducto(producto.getSku());
            producto.setCodigoBarras(codigo);

            // Generar la imagen del código de barras
            generarImagenCodigoBarras(codigo, producto.getSku());
        }
        log.info("Producto ID antes de save: {}", producto.getId());
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
        log.info("Intentando eliminar producto con ID: {}", id);

        if (id == null) {
            log.warn("Intento de eliminación con ID null");
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        if (!repository.existsById(id)) {
            log.warn("Intento de eliminar producto inexistente. ID: {}", id);
            throw new IllegalArgumentException("El producto no existe");
        }

        repository.deleteById(id);
        log.info("Producto eliminado correctamente. ID: {}", id);
    }

    private void validarProducto(Producto producto) {

        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }

        log.info("Validando producto ID: {}", producto.getId());

        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (producto.getPrecioVenta() == null ||
                producto.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio de venta debe ser mayor a 0");
        }

        if (producto.getCosto() != null &&
                producto.getCosto().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo");
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
            csv.append("ID,Nombre,Categoria,Talle,Color,SKU,CodigoBarras,Costo,PrecioVenta,Stock,StockMinimo,Activo\n");

            for (Producto p : productos) {
                csv.append(p.getId()).append(",")
                        .append(p.getSku()).append(",")
                        .append(p.getNombre()).append(",")
                        .append(p.getSku()).append(",")
                        .append(p.getCodigoBarras()).append(",")
                        .append(p.getCosto()).append(",")
                        .append(p.getPrecioVenta()).append(",")
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
                p.getCodigoBarras(),
                p.getCosto(),
                p.getPrecioVenta(),
                p.getActivo(),
                p.getProveedor().getNombre()
        );
    }

    public void generarImagenCodigoBarras(String codigo, String sku) {
        try {
            // Generamos el BitMatrix con el código de barras
            Code128Writer barcodeWriter = new Code128Writer();
            BitMatrix bitMatrix = barcodeWriter.encode(codigo, BarcodeFormat.CODE_128, 300, 100);

            // Nos aseguramos de que exista la carpeta /barcodes
            Path directorio = Paths.get("barcodes");
            if (!Files.exists(directorio)) {
                Files.createDirectories(directorio);
            }

            // Definimos el path completo del archivo de salida
            Path rutaArchivo = directorio.resolve(sku + ".png");

            // Escribimos la imagen en disco
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", rutaArchivo);

        } catch (IOException e) {
            throw new RuntimeException("Error generando código de barras", e);
        }
    }

    private String generarCodigoBarrasParaProducto(String sku) {
        // Lógica simple: prefijo + SKU + timestamp para que sea único
        return "BRK-" + sku + "-" + System.currentTimeMillis();
    }
}
