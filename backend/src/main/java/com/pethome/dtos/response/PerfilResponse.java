package com.pethome.dtos.response;

import com.pethome.models.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerfilResponse {

    private Long id;
    private String nombreUsuario;
    private String email;
    private Role rol;
    private String nombreCompleto;
    private String direccion;
    private String ciudad;
    private String provincia;
    private String telefono;
    private Integer edad;
}