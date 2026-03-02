package com.breakingrules.stock.cuentaCorriente.entity;

import com.breakingrules.stock.clientes.entity.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CuentaCorriente {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Builder.Default
        @Column(nullable = false, precision = 15, scale = 2)
        private BigDecimal saldo = BigDecimal.ZERO;

        @Column(nullable = false)
        private Boolean activo = true;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "cliente_id", unique = true, nullable = false)
        private Cliente cliente;

        @OneToMany(mappedBy = "cuentaCorriente", cascade = CascadeType.ALL)
        private List<MovimientoCuenta> movimientos = new ArrayList<>();
}
