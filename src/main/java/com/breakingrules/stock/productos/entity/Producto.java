package com.breakingrules.stock.productos.entity;

import com.breakingrules.stock.clientes.entity.TipoCliente;
import com.breakingrules.stock.productos.validation.PrecioValido;
import com.breakingrules.stock.proveedores.entity.Proveedor;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.*;
import org.hibernate.annotations.Where;

@PrecioValido
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

    @NotBlank(message = "El Articulo es obligatorio")
    @Column(length = 100)
    private String sku;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;

    @Positive(message = "El costo debe ser mayor a 0")
    private BigDecimal costo;

    @NotNull(message = "El precio público es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio público debe ser mayor a 0")
    private BigDecimal precioVentaPublico;

    @NotNull(message = "El precio mayorista es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio mayorista debe ser mayor a 0")
    private BigDecimal precioVentaMayorista;

    private Boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<VarianteProducto> variantes;
}