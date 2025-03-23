package com.host_go.host_go.modelos;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE arrendatario SET status = 'DELETED' WHERE arrendatario_id = ?") // Soft delete
public class Arrendatario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long arrendatario_id;
    private Integer cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private long telefono;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;
}
