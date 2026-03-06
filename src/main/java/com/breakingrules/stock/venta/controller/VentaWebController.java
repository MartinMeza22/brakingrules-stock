package com.breakingrules.stock.venta.controller;

import com.breakingrules.stock.clientes.repository.ClienteRepository;
import com.breakingrules.stock.productos.entity.VarianteProducto;
import com.breakingrules.stock.productos.repository.VarianteProductoRepository;
import com.breakingrules.stock.venta.entity.Venta;
import com.breakingrules.stock.venta.entity.VentaDetalle;
import com.breakingrules.stock.venta.service.VentaService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web/ventas")
public class VentaWebController {

    private final VentaService ventaService;
    private final ClienteRepository clienteRepository;
    private final VarianteProductoRepository varianteRepository;

    public VentaWebController(
            VentaService ventaService,
            ClienteRepository clienteRepository,
            VarianteProductoRepository varianteRepository
    ) {
        this.ventaService = ventaService;
        this.clienteRepository = clienteRepository;
        this.varianteRepository = varianteRepository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("variantes", varianteRepository.findAll());

        return "ventas/crear";
    }

    @PostMapping("/crear")
    public String crearVenta(@RequestParam Integer clienteId) {

        var venta = ventaService.crearVenta(clienteId);

        return "redirect:/web/ventas/" + venta.getId();
    }

    @GetMapping("/{ventaId}")
    public String verVenta(@PathVariable Integer ventaId, Model model) {

        model.addAttribute("venta", ventaService.obtenerVenta(ventaId));
        model.addAttribute("detalles", ventaService.obtenerDetalles(ventaId));
        model.addAttribute("variantes", varianteRepository.findAll());

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
    public String finalizarVenta(@RequestParam Integer ventaId) {

        ventaService.finalizarVenta(ventaId);

        return "redirect:/web/ventas";
    }

    @GetMapping("/historial")
    public String historial(Model model) {

        model.addAttribute("ventas", ventaService.listarVentas());

        return "ventas/historial";
    }

    @GetMapping("/remito/{id}")
    public String generarRemito(@PathVariable Integer id, Model model) {

        Venta venta = ventaService.obtenerVenta(id);

        List<VentaDetalle> detalles = ventaService.obtenerDetalles(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);

        return "ventas/remito";
    }

    @GetMapping("/ventas")
    public String listarVentas(Model model) {

        model.addAttribute("ventas", ventaService.listarVentas());

        return "ventas/listaVentas";
    }

    @GetMapping("/ventas/{id}")
    public String verDetalleVenta(@PathVariable Integer id, Model model) {

        Venta venta = ventaService.obtenerVenta(id);

        List<VentaDetalle> detalles = ventaService.obtenerDetalles(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);

        return "ventas/detalleVenta";
    }
}