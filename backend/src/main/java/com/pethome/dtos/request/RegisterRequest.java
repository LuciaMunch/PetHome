package com.pethome.dtos.request;

import com.pethome.models.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {
    private String nombre_usuario;
    private String contraseña;
    private String email;
    private Role rol;
}
