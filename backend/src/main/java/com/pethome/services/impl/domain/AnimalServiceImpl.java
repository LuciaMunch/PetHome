package com.pethome.services.impl.domain;

import com.pethome.dtos.request.AnimalRequest;
import com.pethome.dtos.response.AnimalResponse;
import com.pethome.mappers.AnimalMapper;
import com.pethome.models.Animal;
import com.pethome.models.EstadoAnimal;
import com.pethome.repositories.AnimalRepository;
import com.pethome.services.interfaces.domain.AnimalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    public AnimalServiceImpl(AnimalRepository animalRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    @Override
    public AnimalResponse crear(AnimalRequest request) {
        Animal animal = animalMapper.toEntity(request);
        Animal guardado = animalRepository.save(animal);
        return animalMapper.toResponse(guardado);
    }

    @Override
    public List<AnimalResponse> listarTodos() {
        return animalRepository.findAll()
                .stream()
                .map(animalMapper::toResponse)
                .toList();
    }

    @Override
    public AnimalResponse obtenerPorId(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado con id " + id));
        return animalMapper.toResponse(animal);
    }

    @Override
    public AnimalResponse actualizar(Long id, AnimalRequest request) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado con id " + id));

        animal.setNombre(request.getNombre());
        animal.setEspecie(request.getEspecie());
        animal.setSexo(request.getSexo());
        animal.setTamanio(request.getTamanio());
        animal.setEdad(request.getEdad());
        animal.setDescripcion(request.getDescripcion());

        Animal actualizado = animalRepository.save(animal);
        return animalMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new RuntimeException("Animal no encontrado con id " + id);
        }
        animalRepository.deleteById(id);
    }

    @Override
    public void marcarAdoptado(Long id) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado con id " + id));
        animal.setEstado(EstadoAnimal.ADOPTADO);
        animalRepository.save(animal);
    }
}
