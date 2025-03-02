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
@Where(clause = "status = 0")
@SQLDelete(sql = "UPDATE application SET status = 1 WHERE id = ?")
public class Arrendador {
    @Id
    private Integer Cedula;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private int Telefono;

    @OneToOne
    @JoinColumn(name = "Cuenta_id")
    private Cuenta cuenta;

}
