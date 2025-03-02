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
@SQLDelete(sql = "UPDATE calificacion SET status = 'DELETED' WHERE cedula = ?") // Soft delete
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Calificacion_id;
    private int Estrellas;
    private String Comentario;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "Cuenta_id")
    private Cuenta cuenta;
}
