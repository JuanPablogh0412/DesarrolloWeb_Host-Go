package com.host_go.host_go.modelos;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("deprecation")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "status = 'ACTIVE'") // Filtra solo los activos
@SQLDelete(sql = "UPDATE arrendatario SET status = 'DELETED' WHERE cedula = ?") // Soft delete
public class Arrendatario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Arrendatario_id;
    private Integer Cedula;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private long Telefono;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToOne
    @JoinColumn(name = "Cuenta_id")
    private Cuenta cuenta;
}
