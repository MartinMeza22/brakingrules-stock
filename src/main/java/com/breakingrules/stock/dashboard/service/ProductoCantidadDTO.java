package com.breakingrules.stock.dashboard.service;

public class ProductoCantidadDTO {

    private String nombre;
    private Integer cantidad;

    public ProductoCantidadDTO(String nombre, Integer cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

}
