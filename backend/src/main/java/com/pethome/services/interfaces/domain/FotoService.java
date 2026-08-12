package com.pethome.services.interfaces.domain;

import com.pethome.dtos.response.FotoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FotoService {

    FotoResponse subirFoto(Long animalId, MultipartFile archivo);

    List<FotoResponse> listarPorAnimal(Long animalId);

    void eliminar(Long fotoId);
}
