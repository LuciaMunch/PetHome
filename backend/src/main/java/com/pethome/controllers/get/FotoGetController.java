package com.pethome.controllers.get;

import com.pethome.dtos.response.FotoResponse;
import com.pethome.services.interfaces.domain.FotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animales/{animalId}/fotos")
public class FotoGetController {

    private final FotoService fotoService;

    public FotoGetController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    @GetMapping
    public ResponseEntity<List<FotoResponse>> listarPorAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(fotoService.listarPorAnimal(animalId));
    }
}
