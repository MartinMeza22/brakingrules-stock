package com.breakingrules.stock.productos.service;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.dto.ProductoStatsDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductoService {

    List<ProductoDTO> listarTodosSinPaginacion();

    Producto guardar(Producto producto);

    Producto actualizar(Producto producto);

    void eliminar(Integer id);

    List<ProductoDTO> buscarPorNombre(String nombre);

    Producto obtenerEntidadPorId(Integer id);

    Page<ProductoDTO> listarPaginado(int page, int size);

    boolean existeSku(String sku);

    List<VarianteProducto> obtenerVariantesOrdenadas(Integer productoId);

    String exportarCSV();

    Integer obtenerStockTotal(Integer productoId);
}