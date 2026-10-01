package com.pethome.controllers.put;

import com.pethome.dtos.request.PerfilRequest;
import com.pethome.dtos.response.PerfilResponse;
import com.pethome.models.User;
import com.pethome.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPutController {

    private final UserRepository userRepository;

    public UsuarioPutController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PerfilResponse> actualizarMiPerfil(@AuthenticationPrincipal String nombreUsuario,
                                                             @RequestBody PerfilRequest request) {

        User user = userRepository.findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getDireccion() != null) {
            user.setDireccion(request.getDireccion());
        }
        if (request.getCiudad() != null) {
            user.setCiudad(request.getCiudad());
        }
        if (request.getProvincia() != null) {
            user.setProvincia(request.getProvincia());
        }
        if (request.getTelefono() != null) {
            user.setTelefono(request.getTelefono());
        }
        if (request.getEdad() != null) {
            user.setEdad(request.getEdad());
        }

        userRepository.save(user);

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
        return ResponseEntity.ok(perfil);
    }
}