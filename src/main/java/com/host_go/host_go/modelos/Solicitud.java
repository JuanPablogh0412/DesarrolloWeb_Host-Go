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
@SQLDelete(sql = "UPDATE solicitud SET status = 'DELETED' WHERE solicitudId = ?") // Soft delete
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long solicitudId;
    private String fechaInicio;
    private String fechaFin;
    private int cantidadPer;
    private int costoTotal;

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    @ManyToOne
    @JoinColumn(name = "propiedadId")
    private Propiedad propiedad;
    
    @ManyToOne
    @JoinColumn(name = "arrendatarioId")
    private Arrendatario arrendatario;
}
