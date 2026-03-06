package com.breakingrules.stock.dashboard.controller;

import com.breakingrules.stock.dashboard.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/web/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("ventasHoy", dashboardService.ventasHoy());
        model.addAttribute("ventasMes", dashboardService.ventasMes());
        model.addAttribute("clientes", dashboardService.totalClientes());
        model.addAttribute("productos", dashboardService.totalProductos());
        model.addAttribute("stockTotal", dashboardService.stockTotal());
        model.addAttribute("ventas", dashboardService.ultimasVentas());

        return "dashboard/index";
    }
}