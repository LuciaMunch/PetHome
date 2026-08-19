package com.pethome.dtos.response;

import com.pethome.models.TipoEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class EventoSanitarioResponse {

    private Long id;
    private TipoEvento tipo;
    private LocalDate fecha;
    private String observaciones;
    private Long animalId;
}