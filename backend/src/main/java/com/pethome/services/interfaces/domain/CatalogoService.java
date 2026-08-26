package com.pethome.services.interfaces.domain;

import com.pethome.dtos.response.AnimalResponse;
import com.pethome.models.Especie;
import com.pethome.models.Sexo;
import com.pethome.models.Tamanio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CatalogoService {

    Page<AnimalResponse> listarDisponibles(Especie especie,
                                           Sexo sexo,
                                           Tamanio tamanio,
                                           Integer edadMax,
                                           Pageable pageable);
}
