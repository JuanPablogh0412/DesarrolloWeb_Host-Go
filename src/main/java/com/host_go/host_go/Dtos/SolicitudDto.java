package com.host_go.host_go.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDto {
    private long solicitud_id;
    private String fechaInicio;
    private String fechaFin;
    private int cantidadPer;
    private int costoTotal;

    private PropiedadDto propiedad;
    private ArrendatarioDto arrendatario;
}
