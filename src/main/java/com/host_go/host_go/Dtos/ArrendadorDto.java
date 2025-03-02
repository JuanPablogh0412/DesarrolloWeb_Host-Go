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
    private Integer Cedula;
    private String Nombre;
    private String Apellido;
    private String Correo;
    private int Telefono;
    private CuentaDto cuenta;

}