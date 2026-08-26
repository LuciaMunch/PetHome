package com.pethome.mappers;

import com.pethome.dtos.request.AnimalRequest;
import com.pethome.dtos.response.AnimalResponse;
import com.pethome.models.Animal;
import com.pethome.models.EstadoAnimal;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {

    // De lo que manda el cliente (Request) a una entidad nueva para guardar
    public Animal toEntity(AnimalRequest request) {
        return Animal.builder()
                .nombre(request.getNombre())
                .especie(request.getEspecie())
                .sexo(request.getSexo())
                .tamanio(request.getTamanio())
                .edad(request.getEdad())
                .descripcion(request.getDescripcion())
                .estado(EstadoAnimal.DISPONIBLE)
                .build();
    }

    // De la entidad de la base al Response que devolvemos
    public AnimalResponse toResponse(Animal animal) {
        return AnimalResponse.builder()
                .id(animal.getId())
                .nombre(animal.getNombre())
                .especie(animal.getEspecie())
                .sexo(animal.getSexo())
                .tamanio(animal.getTamanio())
                .edad(animal.getEdad())
                .descripcion(animal.getDescripcion())
                .estado(animal.getEstado())
                .build();
    }
}
