package com.pethome.services.interfaces.domain;

import com.pethome.models.EventoSanitario;

import java.util.List;

public interface EventoSanitarioService {

    EventoSanitario registrarEvento(EventoSanitario evento, Long animalId);

    List<EventoSanitario> obtenerHistorialPorAnimal(Long animalId);
}