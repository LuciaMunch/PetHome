package com.pethome.mappers;

import com.pethome.dtos.request.EventoSanitarioRequest;
import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.models.EventoSanitario;
import org.springframework.stereotype.Component;

@Component
public class EventoSanitarioMapper {

    public EventoSanitario toEntity(EventoSanitarioRequest request) {
        return EventoSanitario.builder()
                .tipo(request.getTipo())
                .fecha(request.getFecha())
                .observaciones(request.getObservaciones())
                .build();
    }

    public EventoSanitarioResponse toResponse(EventoSanitario evento) {
        return new EventoSanitarioResponse(
                evento.getId(),
                evento.getTipo(),
                evento.getFecha(),
                evento.getObservaciones(),
                evento.getAnimal().getId()
        );
    }
}