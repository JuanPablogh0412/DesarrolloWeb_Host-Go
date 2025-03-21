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
    private long propiedad_id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private int precio;
    private String tipo;
    private int capacidad;

    private ArrendadorDto arrendador;
}
