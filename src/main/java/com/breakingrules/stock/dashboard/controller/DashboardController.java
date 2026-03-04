package com.breakingrules.stock.dashboard.controller;

import com.breakingrules.stock.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/web/dashboard")
    public String verDashboard(Model model) {
        model.addAttribute("totalVentasHoy", dashboardService.totalVentasHoy());
        model.addAttribute("totalIngresosHoy", dashboardService.totalIngresosHoy());
        model.addAttribute("totalDeuda", dashboardService.totalDeuda());
        model.addAttribute("totalAFavor", dashboardService.totalAFavor());
        model.addAttribute("clientesConDeuda", dashboardService.clientesConDeuda());
        //model.addAttribute("productosCriticos", dashboardService.productosCriticos(5));

        return "dashboard/index";
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        model.addAttribute("ventasHoy", dashboardService.obtenerVentasDelDia());
        model.addAttribute("ingresosHoy", dashboardService.obtenerIngresosDelDia());
        model.addAttribute("deudaTotal", dashboardService.obtenerDeudaTotal());
        model.addAttribute("saldoAFavor", dashboardService.obtenerSaldoAFavorTotal());
        model.addAttribute("topProductos", dashboardService.topProductosVendidos(5));
        return "dashboard/index";
    }
}