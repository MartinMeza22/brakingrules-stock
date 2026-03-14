package com.breakingrules.stock.productos.dto;

import com.breakingrules.stock.productos.entity.Talle;

import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;
    private String sku;
    private String nombre;

    private BigDecimal costo;
    private BigDecimal precioVentaPublico;
    private BigDecimal precioVentaMayorista;


    private Boolean activo;
    private String proveedorNombre;
    public ProductoDTO() {}

    public ProductoDTO(Integer id, String sku, String nombre,
                       BigDecimal costo, BigDecimal precioVentaPublico, BigDecimal precioVentaMayorista, Boolean activo, String proveedorNombre) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.costo = costo;
        this.precioVentaPublico = precioVentaPublico;
        this.precioVentaMayorista = precioVentaMayorista;
        this.activo = activo;
        this.proveedorNombre = proveedorNombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getPrecioVentaPublico() {
        return precioVentaPublico;
    }

    public void setPrecioVentaPublico(BigDecimal precioVentaPublico) {
        this.precioVentaPublico = precioVentaPublico;
    }

    public BigDecimal getPrecioVentaMayorista() {
        return precioVentaMayorista;
    }

    public void setPrecioVentaMayorista(BigDecimal precioVentaMayorista) {
        this.precioVentaMayorista = precioVentaMayorista;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getProveedorNombre() {
        return proveedorNombre;
    }

    public void setProveedorNombre(String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
}