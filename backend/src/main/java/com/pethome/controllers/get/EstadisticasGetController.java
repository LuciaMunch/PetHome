package com.pethome.controllers.get;

import com.pethome.dtos.response.EstadisticasResponse;
import com.pethome.services.interfaces.domain.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticasGetController {

    private final EstadisticasService estadisticasService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EstadisticasResponse resumen() {
        return estadisticasService.obtenerResumen();
    }
}