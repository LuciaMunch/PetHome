package com.pethome.controllers.get;

import com.pethome.dtos.response.AnimalResponse;
import com.pethome.models.Especie;
import com.pethome.models.Sexo;
import com.pethome.models.Tamanio;
import com.pethome.services.interfaces.domain.CatalogoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoGetController {

    private final CatalogoService catalogoService;

    public CatalogoGetController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public ResponseEntity<Page<AnimalResponse>> listarDisponibles(
            @RequestParam(required = false) Especie especie,
            @RequestParam(required = false) Sexo sexo,
            @RequestParam(required = false) Tamanio tamanio,
            @RequestParam(required = false) Integer edadMax,
            Pageable pageable) {

        return ResponseEntity.ok(
                catalogoService.listarDisponibles(especie, sexo, tamanio, edadMax, pageable));
    }
}
