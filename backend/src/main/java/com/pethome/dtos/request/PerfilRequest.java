package com.pethome.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilRequest {

    private String direccion;
    private String ciudad;
    private String provincia;
    private String telefono;
    private Integer edad;
}