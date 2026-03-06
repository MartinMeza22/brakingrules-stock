package com.breakingrules.stock.productos.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "variantes_producto",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_producto_color_talle",
                        columnNames = {"producto_id", "color", "talle"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Producto producto;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private Talle talle;

    private Integer stock;
}
