package com.pethome.controllers.post;

import com.pethome.dtos.request.EventoSanitarioRequest;
import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.services.interfaces.domain.EventoSanitarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos-sanitarios")
public class EventoSanitarioPostController {

    private final EventoSanitarioService service;

    public EventoSanitarioPostController(EventoSanitarioService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EventoSanitarioResponse> registrar(@RequestBody EventoSanitarioRequest request) {
        return ResponseEntity.ok(service.registrar(request));
    }
}