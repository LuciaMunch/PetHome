package com.pethome.controllers.post;
import org.springframework.security.access.prepost.PreAuthorize;
import com.pethome.dtos.request.EventoSanitarioRequest;
import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.mappers.EventoSanitarioMapper;
import com.pethome.models.EventoSanitario;
import com.pethome.services.interfaces.domain.EventoSanitarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/eventos-sanitarios")
@RequiredArgsConstructor
public class EventoSanitarioPostController {

    private final EventoSanitarioService eventoSanitarioService;
    private final EventoSanitarioMapper eventoSanitarioMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventoSanitarioResponse> registrarEvento(
            @Valid @RequestBody EventoSanitarioRequest request) {

        EventoSanitario evento = eventoSanitarioMapper.toEntity(request);
        EventoSanitario guardado = eventoSanitarioService.registrarEvento(evento, request.getAnimalId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventoSanitarioMapper.toResponse(guardado));
    }
}