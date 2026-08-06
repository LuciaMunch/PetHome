package com.pethome.services.impl.domain;

import com.pethome.dtos.response.AnimalResponse;
import com.pethome.mappers.AnimalMapper;
import com.pethome.models.EstadoAnimal;
import com.pethome.models.Especie;
import com.pethome.models.Sexo;
import com.pethome.models.Tamanio;
import com.pethome.repositories.AnimalRepository;
import com.pethome.services.interfaces.domain.CatalogoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    public CatalogoServiceImpl(AnimalRepository animalRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    @Override
    public Page<AnimalResponse> listarDisponibles(Especie especie,
                                                  Sexo sexo,
                                                  Tamanio tamanio,
                                                  Integer edadMax,
                                                  Pageable pageable) {
        return animalRepository
                .buscarCatalogo(EstadoAnimal.DISPONIBLE, especie, sexo, tamanio, edadMax, pageable)
                .map(animalMapper::toResponse);
    }
}
