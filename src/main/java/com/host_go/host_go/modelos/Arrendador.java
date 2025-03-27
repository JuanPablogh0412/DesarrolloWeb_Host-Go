package com.host_go.host_go.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE arrendador SET status = 'DELETED' WHERE arrendadorId = ?") // Soft delete
public class Arrendador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long arrendadorId;
    private Integer cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private long telefono;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @OneToOne
    @JoinColumn(name = "cuentaId")
    private Cuenta cuenta;
}
