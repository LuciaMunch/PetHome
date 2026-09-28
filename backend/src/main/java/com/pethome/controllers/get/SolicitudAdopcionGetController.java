package com.pethome.controllers.get;

import com.pethome.dtos.response.SolicitudAdopcionResponse;
import com.pethome.models.User;
import com.pethome.repositories.UserRepository;
import com.pethome.services.interfaces.domain.SolicitudAdopcionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-adopcion")
@RequiredArgsConstructor
public class SolicitudAdopcionGetController {

    private final SolicitudAdopcionService solicitudAdopcionService;
    private final UserRepository userRepository;

    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasRole('ADOPTANTE')")
    public List<SolicitudAdopcionResponse> misSolicitudes(Authentication authentication) {
        User usuarioActual = obtenerUsuarioActual(authentication);
        return solicitudAdopcionService.obtenerMisSolicitudes(usuarioActual.getId());
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<SolicitudAdopcionResponse> pendientes(Pageable pageable) {
        return solicitudAdopcionService.obtenerPendientes(pageable);
    }

    private User obtenerUsuarioActual(Authentication authentication) {
        return userRepository.findByNombreUsuario(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }
}