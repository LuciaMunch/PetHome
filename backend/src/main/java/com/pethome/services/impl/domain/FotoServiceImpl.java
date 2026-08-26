package com.pethome.services.impl.domain;

import com.pethome.dtos.response.FotoResponse;
import com.pethome.models.Animal;
import com.pethome.models.Foto;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.FotoRepository;
import com.pethome.services.interfaces.commons.CloudinaryService;
import com.pethome.services.interfaces.domain.FotoService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FotoServiceImpl implements FotoService {

    private final FotoRepository fotoRepository;
    private final AnimalRepository animalRepository;
    private final CloudinaryService cloudinaryService;

    public FotoServiceImpl(FotoRepository fotoRepository,
                           AnimalRepository animalRepository,
                           CloudinaryService cloudinaryService) {
        this.fotoRepository = fotoRepository;
        this.animalRepository = animalRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public FotoResponse subirFoto(Long animalId, MultipartFile archivo) {
        // 1. Verificar que el animal exista
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new RuntimeException("Animal no encontrado con id " + animalId));

        // 2. Subir el archivo a Cloudinary y obtener la URL
        String url = cloudinaryService.subirImagen(archivo);

        // 3. Guardar la foto (solo la URL) asociada al animal
        Foto foto = Foto.builder()
                .url(url)
                .animal(animal)
                .build();
        Foto guardada = fotoRepository.save(foto);

        // 4. Devolver el response
        return toResponse(guardada);
    }

    @Override
    public List<FotoResponse> listarPorAnimal(Long animalId) {
        return fotoRepository.findByAnimalId(animalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void eliminar(Long fotoId) {
        if (!fotoRepository.existsById(fotoId)) {
            throw new RuntimeException("Foto no encontrada con id " + fotoId);
        }
        fotoRepository.deleteById(fotoId);
    }

    private FotoResponse toResponse(Foto foto) {
        return FotoResponse.builder()
                .id(foto.getId())
                .url(foto.getUrl())
                .animalId(foto.getAnimal().getId())
                .build();
    }
}
