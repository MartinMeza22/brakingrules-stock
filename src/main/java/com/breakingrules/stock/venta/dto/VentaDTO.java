package com.breakingrules.stock.venta.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDTO {

    private Integer id;

    @NotNull(message = "Debe seleccionar un cliente")
    private Integer clienteId;

    @NotBlank(message = "Debe elegir una forma de pago")
    private String formaPago;

    @NotEmpty(message = "Debe agregar al menos un producto")
    @Valid
    private List<ItemVentaDTO> items  = new ArrayList<>();

    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoPagado;

    private BigDecimal vuelto;
    private LocalDateTime fecha;

    public VentaDTO() {
    }

    public VentaDTO(Integer id, Integer clienteId, BigDecimal montoPagado, BigDecimal vuelto, List<ItemVentaDTO> items, String formaPago, LocalDateTime fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.montoPagado = montoPagado;
        this.vuelto = vuelto;
        this.items = items;
        this.formaPago = formaPago;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemVentaDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemVentaDTO> items) {
        this.items = items;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public BigDecimal getVuelto() {
        return vuelto;
    }

    public void setVuelto(BigDecimal vuelto) {
        this.vuelto = vuelto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
