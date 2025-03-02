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
    private long Propiedad_id;
    private String Nombre;
    private String Descripcion;
    private String Ubicacion;
    private int Precio;
    private String Tipo;
    private int Capacidad;
    private int Estado;

    private ArrendadorDto arrendador;
}
