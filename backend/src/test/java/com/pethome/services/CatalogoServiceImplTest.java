package com.pethome.services;

import com.pethome.dtos.response.AnimalResponse;
import com.pethome.mappers.AnimalMapper;
import com.pethome.models.Animal;
import com.pethome.models.EstadoAnimal;
import com.pethome.models.Especie;
import com.pethome.repositories.AnimalRepository;
import com.pethome.services.impl.domain.CatalogoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas UNITARIAS del módulo de Catálogo público.
 */
@ExtendWith(MockitoExtension.class)
class CatalogoServiceImplTest {

    @Mock private AnimalRepository animalRepository;
    @Mock private AnimalMapper animalMapper;

    @InjectMocks private CatalogoServiceImpl servicio;

    @Test
    @DisplayName("listarDisponibles: consulta el catálogo filtrando SIEMPRE por estado DISPONIBLE")
    void listarDisponibles_filtraPorDisponible() {
        Pageable pageable = PageRequest.of(0, 10);
        when(animalRepository.buscarCatalogo(eq(EstadoAnimal.DISPONIBLE),
                any(), any(), any(), any(), eq(pageable)))
                .thenReturn(Page.<Animal>empty());

        Page<AnimalResponse> resultado =
                servicio.listarDisponibles(Especie.PERRO, null, null, null, pageable);

        assertNotNull(resultado);
        // Se asegura de que el catálogo nunca expone animales no disponibles
        verify(animalRepository).buscarCatalogo(eq(EstadoAnimal.DISPONIBLE),
                eq(Especie.PERRO), isNull(), isNull(), isNull(), eq(pageable));
    }

    @Test
    @DisplayName("obtenerDetalle: si el animal no existe -> lanza EntityNotFoundException")
    void obtenerDetalle_inexistente() {
        when(animalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.obtenerDetalle(99L));
    }
}
