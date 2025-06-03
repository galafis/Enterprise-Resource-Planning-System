package com.galafis.erp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Dashboard Controller
 * Handles main dashboard and navigation
 * 
 * @author Gabriel Demetrios Lafis
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Dashboard - ERP System");
        model.addAttribute("activeModule", "dashboard");
        return "dashboard/index";
    }
    
    @GetMapping("/inventory")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('INVENTORY')")
    public String inventory(Model model) {
        model.addAttribute("pageTitle", "Inventory Management");
        model.addAttribute("activeModule", "inventory");
        return "inventory/index";
    }
    
    @GetMapping("/finance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('FINANCE')")
    public String finance(Model model) {
        model.addAttribute("pageTitle", "Financial Management");
        model.addAttribute("activeModule", "finance");
        return "finance/index";
    }
    
    @GetMapping("/hr")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('HR')")
    public String humanResources(Model model) {
        model.addAttribute("pageTitle", "Human Resources");
        model.addAttribute("activeModule", "hr");
        return "hr/index";
    }
    
    @GetMapping("/crm")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public String customerRelations(Model model) {
        model.addAttribute("pageTitle", "Customer Relationship Management");
        model.addAttribute("activeModule", "crm");
        return "crm/index";
    }
}
