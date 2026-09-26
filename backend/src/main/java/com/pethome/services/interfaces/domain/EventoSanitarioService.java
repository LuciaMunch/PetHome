package com.pethome.services.interfaces.domain;

import com.pethome.dtos.request.EventoSanitarioRequest;
import com.pethome.dtos.response.EventoSanitarioResponse;

import java.util.List;

public interface EventoSanitarioService {

    List<EventoSanitarioResponse> obtenerPorAnimal(Long animalId);

    EventoSanitarioResponse registrar(EventoSanitarioRequest request);

    void eliminar(Long id);

}