package com.host_go.host_go.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PropiedadDto {
    private long propiedadId;
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

    private ArrendadorDto arrendador;
}
