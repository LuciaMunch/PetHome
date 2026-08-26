package com.pethome.controllers.get;

import com.pethome.dtos.response.AnimalResponse;
import com.pethome.services.interfaces.domain.AnimalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animales")
public class AnimalGetController {

    private final AnimalService animalService;

    public AnimalGetController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> listarTodos() {
        return ResponseEntity.ok(animalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.obtenerPorId(id));
    }
}
