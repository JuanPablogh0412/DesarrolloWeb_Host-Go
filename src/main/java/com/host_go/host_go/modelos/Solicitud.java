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
@SQLDelete(sql = "UPDATE solicitud SET status = 'DELETED' WHERE solicitud_id = ?") // Soft delete
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long solicitud_id;
    private String fechaInicio;
    private String fechaFin;
    private int cantidadPer;
    private int costoTotal;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;
    
    @ManyToOne
    @JoinColumn(name = "arrendatario_id")
    private Arrendatario arrendatario;
}
