package com.breakingrules.stock.cuentaCorriente.controller;

import com.breakingrules.stock.cuentaCorriente.service.CuentaCorrienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/web/cuentas")
@RequiredArgsConstructor
public class CuentaCorrienteWebController {

    private final CuentaCorrienteService cuentaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("cuentas", cuentaService.listarTodas());
        return "cuentas/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model) {
        var cuenta = cuentaService.obtenerPorId(id);
        model.addAttribute("cuenta", cuenta);
        model.addAttribute("movimientos", cuenta.getMovimientos());
        return "cuentas/detalle";
    }

    @PostMapping("/pago")
    public String registrarPago(@RequestParam Integer clienteId,
                                @RequestParam BigDecimal monto,
                                @RequestParam String descripcion) {

        cuentaService.registrarPago(clienteId, monto, descripcion);
        return "redirect:/web/cuentas";
    }
}