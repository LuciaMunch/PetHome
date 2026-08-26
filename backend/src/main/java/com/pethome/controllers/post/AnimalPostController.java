package com.pethome.controllers.post;

import com.pethome.dtos.request.AnimalRequest;
import com.pethome.dtos.response.AnimalResponse;
import com.pethome.services.interfaces.domain.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animales")
public class AnimalPostController {

    private final AnimalService animalService;

    public AnimalPostController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    public ResponseEntity<AnimalResponse> crear(@Valid @RequestBody AnimalRequest request) {
        AnimalResponse response = animalService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}