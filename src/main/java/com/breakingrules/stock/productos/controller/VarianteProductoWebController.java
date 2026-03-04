package com.breakingrules.stock.productos.controller;

import com.breakingrules.stock.productos.entity.Color;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.service.ProductoService;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/variantes")
@RequiredArgsConstructor
public class VarianteProductoWebController {

    private final VarianteProductoService varianteService;
    private final ProductoService productoService;

    @PostMapping("/crear")
    public String crearVariante(
            @RequestParam Integer productoId,
            @RequestParam Color color,
            @RequestParam Talle talle,
            @RequestParam Integer stockInicial
    ) {
        varianteService.crearVariante(productoId, talle, color, stockInicial);
        return "redirect:/web/variantes/" + productoId;
    }

    @PostMapping("/ingresar-stock")
    public String ingresarStock(
            @RequestParam Integer varianteId,
            @RequestParam Integer cantidad
    ) {
        varianteService.ingresarStock(varianteId, cantidad);

        Integer productoId = varianteService
                .obtenerPorId(varianteId)
                .getProducto()
                .getId();

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/{productoId}")
    public String listar(@PathVariable Integer productoId, Model model) {

        Producto producto = productoService.obtenerEntidadPorId(productoId);

        model.addAttribute("producto", producto);
        model.addAttribute("variantes", producto.getVariantes());
        model.addAttribute("colores", Color.values());
        model.addAttribute("talles", Talle.values());
        model.addAttribute("varianteNueva", new VarianteProducto());

        return "variantes/listar";
    }

    @PostMapping("/guardar/{productoId}")
    public String guardar(@PathVariable Integer productoId,
                          @ModelAttribute VarianteProducto variante) {

        Producto producto = productoService.obtenerEntidadPorId(productoId);

        variante.setProducto(producto);

        varianteService.guardar(variante);

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/eliminar/{varianteId}/{productoId}")
    public String eliminar(@PathVariable Integer varianteId,
                           @PathVariable Integer productoId) {

        varianteService.eliminar(varianteId);

        return "redirect:/web/variantes/" + productoId;
    }
}