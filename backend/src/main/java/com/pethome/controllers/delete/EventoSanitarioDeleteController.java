package com.pethome.controllers.delete;

import com.pethome.services.interfaces.domain.EventoSanitarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos-sanitarios")
public class EventoSanitarioDeleteController {

    private final EventoSanitarioService service;

    public EventoSanitarioDeleteController(EventoSanitarioService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}