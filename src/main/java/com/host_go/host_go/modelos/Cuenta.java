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
@SQLDelete(sql = "UPDATE cuenta SET status = 'DELETED' WHERE cuentaId = ?") // Soft delete
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cuentaId;
    private String usuario;
    private String contrasena;
    private String tipo; // Valores posibles: "ARRENDADOR", "ARRENDATARIO", "ADMIN"

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
}
