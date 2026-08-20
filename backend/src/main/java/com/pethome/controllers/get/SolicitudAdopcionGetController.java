package com.pethome.controllers.get;

import com.pethome.dtos.response.SolicitudAdopcionResponse;
import com.pethome.mappers.SolicitudAdopcionMapper;
import com.pethome.models.User;
import com.pethome.services.interfaces.domain.SolicitudAdopcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-adopcion")
@RequiredArgsConstructor
public class SolicitudAdopcionGetController {

    private final SolicitudAdopcionService solicitudAdopcionService;
    private final SolicitudAdopcionMapper solicitudAdopcionMapper;

    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasRole('ADOPTANTE')")
    public List<SolicitudAdopcionResponse> misSolicitudes(@AuthenticationPrincipal User usuarioActual) {
        return solicitudAdopcionService.obtenerMisSolicitudes(usuarioActual.getId())
                .stream()
                .map(solicitudAdopcionMapper::toResponse)
                .toList();
    }

    @GetMapping("/pendientes")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<SolicitudAdopcionResponse> pendientes(Pageable pageable) {
        return solicitudAdopcionService.obtenerPendientes(pageable)
                .map(solicitudAdopcionMapper::toResponse);
    }
}