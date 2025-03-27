package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArrendadorDto {
    private long arrendadorId;
    private Integer cedula;
    private String nombre;
    private String apellido;
    private String correo;
    private long telefono;
    private CuentaDto cuenta;
}