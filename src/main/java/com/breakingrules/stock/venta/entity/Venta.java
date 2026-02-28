package com.breakingrules.stock.venta.entity;

import com.breakingrules.stock.clientes.entity.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime fecha;

    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    private BigDecimal total;

    @DecimalMin(value = "0.0", inclusive = false, message = "El monto pagado debe ser mayor a 0")
    private BigDecimal montoPagado;

    private BigDecimal vuelto;

    @NotBlank(message = "La forma de pago es obligatoria")
    private String formaPago;

    @NotNull(message = "Debe seleccionar un cliente")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}