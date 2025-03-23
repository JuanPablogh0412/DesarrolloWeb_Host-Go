package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrendatarioCreateDto {
    private Integer cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private long telefono;
    private String Contrasena;
}