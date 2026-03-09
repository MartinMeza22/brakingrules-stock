package com.breakingrules.stock.clientes.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @Size(max = 100)
    private String descripcion;

    @NotBlank(message = "El DNI o CUIT es obligatorio")
    @Size(max = 20)
    private String documento;

    @Size(max = 11)
    @Pattern(regexp = "^$|^\\d{11}$", message = "CUIL inválido")
    private String cuil;

    @Email(message = "Email inválido")
    @Size(max = 150)
    private String email;

    @Pattern(regexp = "\\d{6,15}", message = "Teléfono inválido")
    private String telefono;

    @Size(max = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCliente tipoCliente;

    private Boolean tieneCuentaCorriente = true;

    private Boolean activo = true;

    @PrePersist
    public void prePersist() {

        if(tipoCliente == null){
            tipoCliente = TipoCliente.MAYORISTA;
        }

        if(tieneCuentaCorriente == null){
            tieneCuentaCorriente = true;
        }

        if(activo == null){
            activo = true;
        }
    }

}