package com.pethome.controllers.post;

import com.pethome.dtos.request.SolicitudAdopcionRequest;
import com.pethome.dtos.response.SolicitudAdopcionResponse;
import com.pethome.mappers.SolicitudAdopcionMapper;
import com.pethome.models.SolicitudAdopcion;
import com.pethome.models.User;
import com.pethome.services.interfaces.domain.SolicitudAdopcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes-adopcion")
@RequiredArgsConstructor
public class SolicitudAdopcionPostController {

    private final SolicitudAdopcionService solicitudAdopcionService;
    private final SolicitudAdopcionMapper solicitudAdopcionMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADOPTANTE')")
    public ResponseEntity<SolicitudAdopcionResponse> enviarSolicitud(
            @Valid @RequestBody SolicitudAdopcionRequest request,
            @AuthenticationPrincipal User usuarioActual) {

        SolicitudAdopcion solicitud = solicitudAdopcionMapper.toEntity(request);
        SolicitudAdopcion guardada = solicitudAdopcionService.enviarSolicitud(
                solicitud, request.getAnimalId(), usuarioActual.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitudAdopcionMapper.toResponse(guardada));
    }
}