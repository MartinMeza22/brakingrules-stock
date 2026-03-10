package com.breakingrules.stock.dashboard.controller;

import com.breakingrules.stock.dashboard.dto.DashboardDTO;
import com.breakingrules.stock.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public String dashboard(
            @RequestParam(required = false, defaultValue = "MENSUAL") String filtro,
            Model model
    ) {

        DashboardDTO stats = dashboardService.obtenerEstadisticas(filtro);

        model.addAttribute("stats", stats);
        model.addAttribute("filtro", filtro);

        return "dashboard/index";
    }

}