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
        private Integer id;

        private LocalDateTime fecha;

        @Enumerated(EnumType.STRING)
        private EstadoVenta estado;

        private BigDecimal total;

        private BigDecimal descuento;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cliente_id")
        private Cliente cliente;

        @PrePersist
        public void prePersist() {
            this.fecha = LocalDateTime.now();
        }
    }