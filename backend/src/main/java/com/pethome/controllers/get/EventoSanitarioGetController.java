package com.pethome.controllers.get;

import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.services.interfaces.domain.EventoSanitarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos-sanitarios")
public class EventoSanitarioGetController {

    private final EventoSanitarioService service;

    public EventoSanitarioGetController(EventoSanitarioService service) {
        this.service = service;
    }

    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<EventoSanitarioResponse>> obtenerHistorial(@PathVariable Long animalId) {
        return ResponseEntity.ok(service.obtenerPorAnimal(animalId));
    }
}