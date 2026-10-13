package com.pethome.services;

import com.pethome.mappers.AnimalMapper;
import com.pethome.models.Animal;
import com.pethome.models.EstadoAnimal;
import com.pethome.repositories.AnimalRepository;
import com.pethome.services.impl.domain.AnimalServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas UNITARIAS de los cambios de estado del animal.
 * Se enfocan en lo que el tutor pidió validar: "cambios de estado" y el manejo
 * de errores cuando el animal no existe. No levantan Spring ni base de datos.
 */
@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {

    @Mock private AnimalRepository animalRepository;
    @Mock private AnimalMapper animalMapper;

    @InjectMocks private AnimalServiceImpl servicio;

    private Animal animalConEstado(Long id, EstadoAnimal estado) {
        return Animal.builder().id(id).nombre("Michi").estado(estado).build();
    }

    @Test
    @DisplayName("marcarAdoptado: cambia el estado a ADOPTADO y guarda")
    void marcarAdoptado_existente() {
        Animal animal = animalConEstado(1L, EstadoAnimal.EN_PROCESO);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        servicio.marcarAdoptado(1L);

        assertEquals(EstadoAnimal.ADOPTADO, animal.getEstado());
        verify(animalRepository).save(animal);
    }

    @Test
    @DisplayName("marcarEnProceso: cambia el estado a EN_PROCESO y guarda")
    void marcarEnProceso_existente() {
        Animal animal = animalConEstado(1L, EstadoAnimal.DISPONIBLE);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        servicio.marcarEnProceso(1L);

        assertEquals(EstadoAnimal.EN_PROCESO, animal.getEstado());
        verify(animalRepository).save(animal);
    }

    @Test
    @DisplayName("marcarDisponible: cambia el estado a DISPONIBLE y guarda")
    void marcarDisponible_existente() {
        Animal animal = animalConEstado(1L, EstadoAnimal.EN_PROCESO);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        servicio.marcarDisponible(1L);

        assertEquals(EstadoAnimal.DISPONIBLE, animal.getEstado());
        verify(animalRepository).save(animal);
    }

    @Test
    @DisplayName("marcarAdoptado: si el animal no existe -> lanza EntityNotFoundException")
    void marcarAdoptado_inexistente() {
        when(animalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.marcarAdoptado(99L));
        verify(animalRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorId: si el animal no existe -> lanza EntityNotFoundException")
    void obtenerPorId_inexistente() {
        when(animalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.obtenerPorId(99L));
    }

    @Test
    @DisplayName("eliminar: si el animal no existe -> lanza EntityNotFoundException y no borra")
    void eliminar_inexistente() {
        when(animalRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> servicio.eliminar(99L));
        verify(animalRepository, never()).deleteById(any());
    }
}
