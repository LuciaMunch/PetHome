package com.pethome.mappers;

import com.pethome.dtos.request.AnimalRequest;
import com.pethome.dtos.response.AnimalResponse;
import com.pethome.models.Animal;
import com.pethome.models.EstadoAnimal;
import com.pethome.models.Foto;
import com.pethome.repositories.FotoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimalMapper {

    private final FotoRepository fotoRepository;

    public AnimalMapper(FotoRepository fotoRepository) {
        this.fotoRepository = fotoRepository;
    }

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
        // Busco las fotos del animal y tomo la URL de la primera (si tiene)
        List<Foto> fotos = fotoRepository.findByAnimalId(animal.getId());
        String fotoUrl = fotos.isEmpty() ? null : fotos.get(0).getUrl();

        return AnimalResponse.builder()
                .id(animal.getId())
                .nombre(animal.getNombre())
                .especie(animal.getEspecie())
                .sexo(animal.getSexo())
                .tamanio(animal.getTamanio())
                .edad(animal.getEdad())
                .descripcion(animal.getDescripcion())
                .estado(animal.getEstado())
                .fotoUrl(fotoUrl)
                .build();
    }
}
