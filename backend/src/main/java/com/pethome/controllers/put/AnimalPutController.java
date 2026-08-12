package com.pethome.controllers.put;

import com.pethome.dtos.request.AnimalRequest;
import com.pethome.dtos.response.AnimalResponse;
import com.pethome.services.interfaces.domain.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animales")
public class AnimalPutController {

    private final AnimalService animalService;

    public AnimalPutController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody AnimalRequest request) {
        return ResponseEntity.ok(animalService.actualizar(id, request));
    }
}
