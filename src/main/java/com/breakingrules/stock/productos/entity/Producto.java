package com.breakingrules.stock.productos.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "productos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_producto_codigo_barras", columnNames = "codigo_barras")
        },
        indexes = {
                @Index(name = "idx_producto_nombre", columnList = "nombre"),
                @Index(name = "idx_producto_codigo_barras", columnList = "codigo_barras")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    private String nombre;

    private String categoria;

    @Enumerated(EnumType.STRING)
    private Talle talle;

    private String color;

    @Column(name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;

    @Column(precision = 15, scale = 2)
    private BigDecimal costo;

    @Column(name = "precio_venta", nullable = false, precision = 15, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(nullable = false)
    private Boolean activo = true;
}