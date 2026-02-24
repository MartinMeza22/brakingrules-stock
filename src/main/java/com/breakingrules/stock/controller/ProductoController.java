package com.breakingrules.stock.controller;

import com.breakingrules.stock.dto.ProductoDTO;
import com.breakingrules.stock.model.Producto;
import com.breakingrules.stock.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoDTO> listar() {
        return service.listar();
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) {
        return service.guardar(producto);
    }
}