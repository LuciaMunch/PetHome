package com.pethome.controllers.post;

import com.pethome.dtos.response.FotoResponse;
import com.pethome.services.interfaces.domain.FotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/animales/{animalId}/fotos")
public class FotoPostController {

    private final FotoService fotoService;

    public FotoPostController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FotoResponse> subirFoto(@PathVariable Long animalId,
                                                  @RequestParam("archivo") MultipartFile archivo) {
        FotoResponse response = fotoService.subirFoto(animalId, archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
