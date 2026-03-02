package com.breakingrules.stock.venta.dto;

import com.breakingrules.stock.caja.entity.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoCajaDTO {
    private Long id;
    private LocalDateTime fecha;
    private TipoMovimiento tipo; // INGRESO / EGRESO
    private BigDecimal monto;
    private String referencia;

    public MovimientoCajaDTO() {
    }

    public MovimientoCajaDTO(Long id, LocalDateTime fecha, TipoMovimiento tipo, BigDecimal monto, String referencia) {
        this.id = id;
        this.fecha = fecha;
        this.tipo = tipo;
        this.monto = monto;
        this.referencia = referencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
