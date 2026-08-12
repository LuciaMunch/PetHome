package com.pethome.controllers.delete;

import com.pethome.services.interfaces.domain.FotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fotos")
public class FotoDeleteController {

    private final FotoService fotoService;

    public FotoDeleteController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    @DeleteMapping("/{fotoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long fotoId) {
        fotoService.eliminar(fotoId);
        return ResponseEntity.noContent().build();
    }
}
