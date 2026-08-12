package com.pethome.controllers.delete;

import com.pethome.services.interfaces.domain.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/animales")
public class AnimalDeleteController {

    private final AnimalService animalService;

    public AnimalDeleteController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        animalService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
