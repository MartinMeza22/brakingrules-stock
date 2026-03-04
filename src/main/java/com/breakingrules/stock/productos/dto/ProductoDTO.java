package com.breakingrules.stock.productos.dto;

import com.breakingrules.stock.productos.entity.Talle;

import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;
    private String sku;
    private String nombre;
    private String codigoBarras;

    private BigDecimal costo;
    private BigDecimal precioVenta;

    private Boolean activo;
    private String proveedorNombre;
    public ProductoDTO() {}

    public ProductoDTO(Integer id, String sku, String nombre, String codigoBarras,
                       BigDecimal costo, BigDecimal precioVenta, Boolean activo, String proveedorNombre) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.codigoBarras = codigoBarras;
        this.costo = costo;
        this.precioVenta = precioVenta;
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

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
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