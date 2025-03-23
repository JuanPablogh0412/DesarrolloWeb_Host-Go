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
@SQLDelete(sql = "UPDATE propiedad SET status = 'DELETED' WHERE propiedad_id  = ?") // Soft delete
public class Propiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long propiedad_id;
    private String nombre;
    private String departamento;
    private String municipio;
    private String tipoIngreso; // Carretera principal, secundaria, terciaria
    private String descripcion;
    private int capacidad;
    private int habitaciones;
    private int banos;
    private boolean permiteMascotas;
    private boolean tienePiscina;
    private boolean tieneAsador;
    private int valorNoche;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    
    @ManyToOne
    @JoinColumn(name = "arrendador_id")
    private Arrendador arrendador;
}
