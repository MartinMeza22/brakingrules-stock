package com.breakingrules.stock.productos.dto;

import com.breakingrules.stock.productos.entity.TipoTalle;

import java.math.BigDecimal;

public class ProductoDTO {

    private Integer id;
    private String sku;
    private String nombre;

    private BigDecimal costo;
    private BigDecimal precioBasePublico;
    private BigDecimal precioBaseMayorista;

    // ✅ PRECIOS ESPECIALES
    private BigDecimal precioEspecial1Publico;
    private BigDecimal precioEspecial2Publico;
    private BigDecimal precioEspecial3Publico;

    private BigDecimal precioEspecial1Mayorista;
    private BigDecimal precioEspecial2Mayorista;
    private BigDecimal precioEspecial3Mayorista;

    private Boolean activo;
    private String proveedorNombre;
    private TipoTalle tipoTalle;

    private Integer stockTotal;
    public ProductoDTO() {}

    public ProductoDTO(Integer id, String sku, String nombre,
                       BigDecimal costo,
                       BigDecimal precioBasePublico,
                       BigDecimal precioBaseMayorista,
                       BigDecimal precioEspecial1Publico,
                       BigDecimal precioEspecial2Publico,
                       BigDecimal precioEspecial3Publico,
                       BigDecimal precioEspecial1Mayorista,
                       BigDecimal precioEspecial2Mayorista,
                       BigDecimal precioEspecial3Mayorista,
                       TipoTalle tipoTalle,
                       Boolean activo,
                       Integer stockTotal,
                       String proveedorNombre) {

        this.id = id;
        this.sku = sku;
        this.nombre = nombre;
        this.costo = costo;
        this.precioBasePublico = precioBasePublico;
        this.precioBaseMayorista = precioBaseMayorista;

        this.precioEspecial1Publico = precioEspecial1Publico;
        this.precioEspecial2Publico = precioEspecial2Publico;
        this.precioEspecial3Publico = precioEspecial3Publico;

        this.precioEspecial1Mayorista = precioEspecial1Mayorista;
        this.precioEspecial2Mayorista = precioEspecial2Mayorista;
        this.precioEspecial3Mayorista = precioEspecial3Mayorista;

        this.stockTotal = stockTotal;
        this.tipoTalle = tipoTalle;
        this.activo = activo;
        this.proveedorNombre = proveedorNombre;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public BigDecimal getCosto() { return costo; }
    public void setCosto(BigDecimal costo) { this.costo = costo; }

    public BigDecimal getPrecioBasePublico() { return precioBasePublico; }
    public void setPrecioBasePublico(BigDecimal precioBasePublico) { this.precioBasePublico = precioBasePublico; }

    public BigDecimal getPrecioBaseMayorista() { return precioBaseMayorista; }
    public void setPrecioBaseMayorista(BigDecimal precioBaseMayorista) { this.precioBaseMayorista = precioBaseMayorista; }

    public BigDecimal getPrecioEspecial1Publico() { return precioEspecial1Publico; }
    public void setPrecioEspecial1Publico(BigDecimal precioEspecial1Publico) { this.precioEspecial1Publico = precioEspecial1Publico; }

    public BigDecimal getPrecioEspecial2Publico() { return precioEspecial2Publico; }
    public void setPrecioEspecial2Publico(BigDecimal precioEspecial2Publico) { this.precioEspecial2Publico = precioEspecial2Publico; }

    public BigDecimal getPrecioEspecial3Publico() { return precioEspecial3Publico; }
    public void setPrecioEspecial3Publico(BigDecimal precioEspecial3Publico) { this.precioEspecial3Publico = precioEspecial3Publico; }

    public BigDecimal getPrecioEspecial1Mayorista() { return precioEspecial1Mayorista; }
    public void setPrecioEspecial1Mayorista(BigDecimal precioEspecial1Mayorista) { this.precioEspecial1Mayorista = precioEspecial1Mayorista; }

    public BigDecimal getPrecioEspecial2Mayorista() { return precioEspecial2Mayorista; }
    public void setPrecioEspecial2Mayorista(BigDecimal precioEspecial2Mayorista) { this.precioEspecial2Mayorista = precioEspecial2Mayorista; }

    public BigDecimal getPrecioEspecial3Mayorista() { return precioEspecial3Mayorista; }
    public void setPrecioEspecial3Mayorista(BigDecimal precioEspecial3Mayorista) { this.precioEspecial3Mayorista = precioEspecial3Mayorista; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public TipoTalle getTipoTalle() {
        return tipoTalle;
    }

    public void setTipoTalle(TipoTalle tipoTalle) {
        this.tipoTalle = tipoTalle;
    }

    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }


    public boolean isTienePreciosEspecialesPublico() {
        return precioEspecial1Publico != null
                || precioEspecial2Publico != null
                || precioEspecial3Publico != null;
    }

    public boolean isTienePreciosEspecialesMayorista() {
        return precioEspecial1Mayorista != null
                || precioEspecial2Mayorista != null
                || precioEspecial3Mayorista != null;
    }

    public Integer getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(Integer stockTotal) {
        this.stockTotal = stockTotal;
    }
}