package com.breakingrules.stock.venta.controller;

import com.breakingrules.stock.clientes.service.ClienteService;
import com.breakingrules.stock.productos.entity.Producto;
import com.breakingrules.stock.productos.service.VarianteProductoService;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;
import com.breakingrules.stock.venta.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/web/ventas")
public class VentaWebController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final VarianteProductoService varianteProductoService;

    public VentaWebController(
            VentaService ventaService,
            ClienteService clienteService,
            VarianteProductoService varianteProductoService
    ) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
        this.varianteProductoService = varianteProductoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientePublico", clienteService.obtenerClientePublico());
        model.addAttribute("mayoristas", clienteService.obtenerMayoristas());
        return "ventas/crear";
    }

    @PostMapping("/crear")
    public String crearVenta(
            @RequestParam Integer clienteId,
            @RequestParam(required = false) String nombreCliente
    ) {

        Venta venta = ventaService.crearVenta(clienteId, nombreCliente);

        return "redirect:/web/ventas/" + venta.getId();
    }


    @GetMapping("/{ventaId}")
    public String verVenta(@PathVariable Integer ventaId, Model model) {

        model.addAttribute("venta", ventaService.obtenerVenta(ventaId));
        model.addAttribute("detalles", ventaService.obtenerDetalles(ventaId));
        model.addAttribute("variantes", varianteProductoService.listarTodas());

        return "ventas/detalle";
    }

    @PostMapping("/agregar-producto")
    public String agregarProducto(
            @RequestParam Integer ventaId,
            @RequestParam Integer varianteId,
            @RequestParam Integer cantidad
    ) {

        ventaService.agregarProducto(ventaId, varianteId, cantidad);

        return "redirect:/web/ventas/" + ventaId;
    }

    @PostMapping("/finalizar")
    public String finalizarVenta(
            @RequestParam Integer ventaId,
            @RequestParam(required = false) BigDecimal descuento
    ) {

        ventaService.finalizarVenta(ventaId, descuento);

        return "redirect:/web/ventas/historial";
    }

    @GetMapping("/historial")
    public String historial(Model model) {

        model.addAttribute("ventas", ventaService.listarVentas());

        return "ventas/historial";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleVenta(@PathVariable Integer id, Model model) {

        Venta venta = ventaService.obtenerVenta(id);
        List<VentaDetalle> detalles = ventaService.obtenerDetalles(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);

        return "ventas/detalleVenta";
    }

    @GetMapping("/remito/{id}")
    public String generarRemito(@PathVariable Integer id, Model model) {
        Venta venta = ventaService.obtenerVenta(id); List<VentaDetalle> detalles = ventaService.obtenerDetalles(id);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        return "ventas/remito"; }
}