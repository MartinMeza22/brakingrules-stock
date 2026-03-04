package com.breakingrules.stock.productos.entity;

import com.breakingrules.stock.proveedores.entity.Proveedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.*;

@Entity
@Table(name = "productos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique = true, length = 100)
    private String sku;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;

    @Positive
    private BigDecimal costo;

    @NotNull
    private BigDecimal precioVenta;

    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<VarianteProducto> variantes;
}