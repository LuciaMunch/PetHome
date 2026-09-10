package com.pethome.dtos.response;

import com.pethome.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class UsuarioResponse {

    private Long id;
    private String nombreUsuario;
    private String email;
    private Role rol;
}