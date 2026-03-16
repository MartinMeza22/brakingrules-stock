package com.breakingrules.stock.productos.dto;

import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;
    private String sku;
    private String nombre;

    private BigDecimal costo;
    private BigDecimal precioBasePublico;
    private BigDecimal precioBaseMayorista;


    private Boolean activo;
    private String proveedorNombre;
    public ProductoDTO() {}

    public ProductoDTO(Integer id, String sku, String nombre,
                       BigDecimal costo, BigDecimal precioBasePublico, BigDecimal precioBaseMayorista, Boolean activo, String proveedorNombre) {
        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.costo = costo;
        this.precioBasePublico = precioBasePublico;
        this.precioBaseMayorista = precioBaseMayorista;
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

    public BigDecimal getPrecioBasePublico() {
        return precioBasePublico;
    }

    public void setPrecioBasePublico(BigDecimal precioBasePublico) {
        this.precioBasePublico = precioBasePublico;
    }

    public BigDecimal getPrecioBaseMayorista() {
        return precioBaseMayorista;
    }

    public void setPrecioBaseMayorista(BigDecimal precioBaseMayorista) {
        this.precioBaseMayorista = precioBaseMayorista;
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