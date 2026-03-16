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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

        varianteService.crearVariante(
                productoId,
                talle,
                color,
                stockInicial
        );

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
                          @ModelAttribute VarianteProducto variante,
                          RedirectAttributes redirectAttributes) {

        Producto producto = productoService.obtenerEntidadPorId(productoId);

        variante.setProducto(producto);

        try {
            varianteService.guardar(variante);
        } catch (RuntimeException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/web/variantes/" + productoId;
        }

        return "redirect:/web/variantes/" + productoId;
    }

    @GetMapping("/eliminar/{varianteId}/{productoId}")
    public String eliminar(@PathVariable Integer varianteId,
                           @PathVariable Integer productoId) {

        varianteService.eliminar(varianteId);

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

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        VarianteProducto variante = varianteService.obtenerPorId(id);

        model.addAttribute("variante", variante);
        model.addAttribute("colores", Color.values());
        model.addAttribute("talles", Talle.values());

        return "variantes/editar";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id,
                             @ModelAttribute VarianteProducto variante) {

        VarianteProducto existente = varianteService.obtenerPorId(id);

        existente.setColor(variante.getColor());
        existente.setTalle(variante.getTalle());
        varianteService.guardar(existente);

        Integer productoId = existente.getProducto().getId();

        return "redirect:/web/variantes/" + productoId;
    }


    @PostMapping("/guardar-multiple/{productoId}")
    public String guardarMultiples(
            @PathVariable Integer productoId,
            @RequestParam Color color,
            @RequestParam(required = false) List<Talle> tallesSeleccionados,
            @RequestParam Map<String, String> stock
    ) {

        if (tallesSeleccionados != null) {

            for (Talle talle : tallesSeleccionados) {

                String valor = stock.get("stock[" + talle.name() + "]");

                Integer cantidad = 0;

                if (valor != null && !valor.isBlank()) {
                    cantidad = Integer.parseInt(valor);
                }

                varianteService.crearVariante(
                        productoId,
                        talle,
                        color,
                        cantidad
                );
            }
        }

        return "redirect:/web/variantes/" + productoId;
    }
}