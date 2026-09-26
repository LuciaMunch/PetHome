package com.pethome.dtos.request;

import com.pethome.models.EventoSanitario;
import java.time.LocalDate;

public record EventoSanitarioRequest(
        EventoSanitario.Tipo tipo,
        LocalDate fecha,
        String observaciones,
        Long animalId
) {}