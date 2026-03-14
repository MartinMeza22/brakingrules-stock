package com.breakingrules.stock.productos.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

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
@Where(clause = "activo = true")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Enumerated(EnumType.STRING)
    private Color color;

    @Enumerated(EnumType.STRING)
    private Talle talle;

    @Column(name = "codigo_barras", unique = true, nullable = false)
    private String codigoBarras;

    private Integer stock;

    @Column(nullable = false)
    private boolean activo = true;
}
