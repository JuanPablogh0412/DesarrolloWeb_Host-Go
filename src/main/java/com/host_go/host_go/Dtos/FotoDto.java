package com.host_go.host_go.Dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FotoDto {

    private long Foto_id;
    private String Url;
    private PropiedadDto propiedad;
}
