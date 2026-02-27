package com.breakingrules.stock.controller;

import com.breakingrules.stock.productos.dto.ProductoDTO;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/productos")
@RequiredArgsConstructor

public class ProductoWebController {

    private final ProductoService service;

    @GetMapping
    public String listar(@RequestParam(required = false) String nombre, Model model) {

        List<ProductoDTO> productos;

        if (nombre != null && !nombre.isEmpty()) {
            productos = service.buscarPorNombre(nombre);
        } else {
            productos = service.listarTodosSinPaginacion();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("productoNuevo", new Producto());

        return "productos";
    }
    @PostMapping
    public String crear(@ModelAttribute Producto producto) {
        service.guardar(producto);
        return "redirect:/web/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return "redirect:/web/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("producto", service.buscarPorId(id));
        return "editar";
    }

    @PostMapping("/editar")
    public String actualizar(@ModelAttribute Producto producto) {
        service.guardar(producto); // save = update si tiene ID
        return "redirect:/web/productos";
    }

//    @GetMapping("/exportarCSV")
//    public String exportarCSV() {
//        service.exportarCSV();
//        return "redirect:/web/productos";
//    }
}