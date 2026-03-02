package com.breakingrules.stock.caja.controller;

import com.breakingrules.stock.caja.service.CajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/caja")
@RequiredArgsConstructor
public class CajaController {

        private final CajaService cajaService;

        @GetMapping
        public String verCaja(Model model) {
            model.addAttribute("movimientos", cajaService.listarMovimientos());
            model.addAttribute("saldo", cajaService.obtenerSaldoActual());
            return "caja/listar";
        }

}
