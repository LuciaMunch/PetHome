package com.pethome.controllers.get;

import com.pethome.dtos.response.PerfilResponse;
import com.pethome.dtos.response.UsuarioResponse;
import com.pethome.mappers.UsuarioMapper;
import com.pethome.models.Role;
import com.pethome.models.User;
import com.pethome.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioGetController {

    private final UserRepository userRepository;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponse> listarAdoptantes() {
        return userRepository.findByRolNot(Role.ADMIN)
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public PerfilResponse obtenerMiPerfil(@AuthenticationPrincipal String nombreUsuario) {

        User user = userRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        PerfilResponse perfil = new PerfilResponse();
        perfil.setId(user.getId());
        perfil.setNombreUsuario(user.getNombreUsuario());
        perfil.setEmail(user.getEmail());
        perfil.setRol(user.getRol());
        perfil.setNombreCompleto(user.getNombreCompleto());
        perfil.setDireccion(user.getDireccion());
        perfil.setCiudad(user.getCiudad());
        perfil.setProvincia(user.getProvincia());
        perfil.setTelefono(user.getTelefono());
        perfil.setEdad(user.getEdad());
        return perfil;
    }
}