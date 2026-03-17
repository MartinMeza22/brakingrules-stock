package com.breakingrules.stock.venta.entity;

import com.breakingrules.stock.clientes.entity.Cliente;
import jakarta.persistence.*;
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

        @Column(nullable = false)
        private LocalDateTime fecha;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private EstadoVenta estado;

        @Column(nullable = false)
        private BigDecimal total;

        @Column(nullable = false)
        private BigDecimal descuento;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "cliente_id", nullable = false)
        private Cliente cliente;

        @Column
        private String nombreClienteMostrador;

        @PrePersist
        public void prePersist() {

                this.fecha = LocalDateTime.now();

                if (this.estado == null) {
                        this.estado = EstadoVenta.ABIERTA;
                }

                if (this.total == null) {
                        this.total = BigDecimal.ZERO;
                }

                if (this.descuento == null) {
                        this.descuento = BigDecimal.ZERO;
                }
        }

        public String getNombreClienteMostrar() {

                if(cliente.getTipoCliente().name().equals("PUBLICO")){

                        if(nombreClienteMostrador != null && !nombreClienteMostrador.isBlank()){
                                return nombreClienteMostrador;
                        }

                        return "Varios";
                }

                return cliente.getNombre() + " " + cliente.getApellido();
        }
}