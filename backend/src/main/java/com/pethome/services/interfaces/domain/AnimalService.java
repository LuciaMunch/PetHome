package com.pethome.services.interfaces.domain;
import com.pethome.dtos.request.AnimalRequest;
import com.pethome.dtos.response.AnimalResponse;

import java.util.List;

    public interface AnimalService {

        AnimalResponse crear(AnimalRequest request);

        List<AnimalResponse> listarTodos();

        AnimalResponse obtenerPorId(Long id);

        AnimalResponse actualizar(Long id, AnimalRequest request);

        void eliminar(Long id);
    }
