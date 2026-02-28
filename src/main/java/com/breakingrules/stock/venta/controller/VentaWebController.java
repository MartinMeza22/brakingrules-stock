package com.breakingrules.stock.venta.controller;

import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.entity.Talle;
import com.breakingrules.stock.venta.dto.ItemVentaDTO;
import com.breakingrules.stock.venta.dto.VentaDTO;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/web/ventas")
@RequiredArgsConstructor
public class VentaWebController {

    private final VentaService ventaService;

    @GetMapping
    public String listar(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        model.addAttribute("formatter", formatter);
        model.addAttribute("ventas", ventaService.obtenerVentas());
        return "ventas/listar";
    }


    @GetMapping("/listar")
    public String listarVentas(Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        model.addAttribute("formatter", formatter);
        model.addAttribute("ventas", ventaService.obtenerVentas());
        return "ventas/listar";
    }

    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        VentaDTO venta = new VentaDTO();
        venta.getItems().add(new ItemVentaDTO());
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", ventaService.obtenerClientes());
        model.addAttribute("productos", ventaService.obtenerProductos());
        return "ventas/nueva";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@Valid @ModelAttribute("venta") VentaDTO ventaDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (result.hasErrors()) {
            model.addAttribute("clientes", ventaService.obtenerClientes());
            model.addAttribute("productos", ventaService.obtenerProductos());
            return "ventas/nueva";
        }

        try {
            ventaService.confirmarVenta(ventaDTO);
            redirectAttributes.addFlashAttribute("success", "Venta registrada correctamente");
            return "redirect:/web/ventas/nueva";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("clientes", ventaService.obtenerClientes());
            model.addAttribute("productos", ventaService.obtenerProductos());
            return "ventas/nueva";
        }
    }

    @GetMapping("/detalle/{id}")  // URL: /web/ventas/detalle/123
    public String detalleVenta(@PathVariable Integer id, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Venta venta = ventaService.findById(id).orElseThrow();
        model.addAttribute("formatter", formatter);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", ventaService.obtenerDetallesVenta(id));
        return "ventas/detalle";
    }

}