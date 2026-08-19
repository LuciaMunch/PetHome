package com.pethome.controllers.get;

import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.mappers.EventoSanitarioMapper;
import com.pethome.services.interfaces.domain.EventoSanitarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos-sanitarios")
@RequiredArgsConstructor
public class EventoSanitarioGetController {

    private final EventoSanitarioService eventoSanitarioService;
    private final EventoSanitarioMapper eventoSanitarioMapper;

    @GetMapping("/animal/{animalId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EventoSanitarioResponse> obtenerHistorial(@PathVariable Long animalId) {
        return eventoSanitarioService.obtenerHistorialPorAnimal(animalId)
                .stream()
                .map(eventoSanitarioMapper::toResponse)
                .toList();
    }
}