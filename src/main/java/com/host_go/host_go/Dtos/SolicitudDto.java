package com.host_go.host_go.Dtos;

import com.host_go.host_go.modelos.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDto {
    private long solicitudId;
    private String fechaInicio;
    private String fechaFin;
    private int cantidadPer;
    private int costoTotal;
    private Status status;

    private PropiedadDto propiedad;
    private ArrendatarioDto arrendatario;
}
