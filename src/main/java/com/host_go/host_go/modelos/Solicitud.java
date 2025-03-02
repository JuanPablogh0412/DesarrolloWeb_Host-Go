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
@SQLDelete(sql = "UPDATE solicitud SET status = 'DELETED' WHERE cedula = ?") // Soft delete
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Solicitud_id;
    private String FechaInicio;
    private String FechaFin;
    private int CantidadPer;
    private int CostoTotal;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "Propiedad_id")
    private Propiedad propiedad;
    
    @ManyToOne
    @JoinColumn(name = "Cedula")
    private Arrendatario arrendatario;
}
