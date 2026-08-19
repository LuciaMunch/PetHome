package com.pethome.dtos.request;

import com.pethome.models.TipoEvento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class EventoSanitarioRequest {

    @NotNull(message = "El tipo de evento es obligatorio")
    private TipoEvento tipo;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String observaciones;

    @NotNull(message = "El id del animal es obligatorio")
    private Long animalId;
}