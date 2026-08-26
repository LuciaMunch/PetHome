package com.pethome.controllers.patch;

import com.pethome.services.interfaces.domain.SolicitudAdopcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes-adopcion")
@RequiredArgsConstructor
public class SolicitudAdopcionPatchController {

    private final SolicitudAdopcionService solicitudAdopcionService;

    @PatchMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> aprobar(@PathVariable Long id) {
        solicitudAdopcionService.aprobarSolicitud(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rechazar(@PathVariable Long id) {
        solicitudAdopcionService.rechazarSolicitud(id);
        return ResponseEntity.ok().build();
    }
}