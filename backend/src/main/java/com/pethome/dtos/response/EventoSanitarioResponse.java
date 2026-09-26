package com.pethome.dtos.response;

import com.pethome.models.EventoSanitario;
import java.time.LocalDate;

public record EventoSanitarioResponse(
        Long id,
        EventoSanitario.Tipo tipo,
        LocalDate fecha,
        String observaciones,
        Long animalId
) {
    public static EventoSanitarioResponse fromEntity(EventoSanitario e) {
        return new EventoSanitarioResponse(
                e.getId(),
                e.getTipo(),
                e.getFecha(),
                e.getObservaciones(),
                e.getAnimal().getId()
        );
    }
}