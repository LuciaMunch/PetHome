package com.pethome.mappers;

import com.pethome.dtos.response.UsuarioResponse;
import com.pethome.models.User;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toResponse(User usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}
