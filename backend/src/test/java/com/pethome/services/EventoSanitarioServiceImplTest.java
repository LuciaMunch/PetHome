package com.pethome.services;

import com.pethome.dtos.request.EventoSanitarioRequest;
import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.models.Animal;
import com.pethome.models.EventoSanitario;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.EventoSanitarioRepository;
import com.pethome.services.impl.domain.EventoSanitarioServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas UNITARIAS del módulo de Seguimiento Sanitario.
 * Cubren el registro de un evento, la consulta por animal y la eliminación.
 */
@ExtendWith(MockitoExtension.class)
class EventoSanitarioServiceImplTest {

    @Mock private EventoSanitarioRepository eventoRepo;
    @Mock private AnimalRepository animalRepo;

    @InjectMocks private EventoSanitarioServiceImpl servicio;

    private Animal animal(Long id) {
        return Animal.builder().id(id).nombre("Firulais").build();
    }

    @Test
    @DisplayName("registrar: con animal existente -> guarda el evento y devuelve la respuesta")
    void registrar_caminoFeliz() {
        Animal animal = animal(1L);
        EventoSanitarioRequest req = new EventoSanitarioRequest(
                EventoSanitario.Tipo.VACUNA, LocalDate.of(2026, 10, 1), "Antirrábica", 1L);

        when(animalRepo.findById(1L)).thenReturn(Optional.of(animal));
        // Devuelve el mismo evento que recibe, pero con id asignado (como haría la base)
        when(eventoRepo.save(any(EventoSanitario.class))).thenAnswer(inv -> {
            EventoSanitario e = inv.getArgument(0);
            e.setId(99L);
            return e;
        });

        EventoSanitarioResponse resp = servicio.registrar(req);

        assertEquals(99L, resp.id());
        assertEquals(EventoSanitario.Tipo.VACUNA, resp.tipo());
        assertEquals(1L, resp.animalId());
        verify(eventoRepo).save(any(EventoSanitario.class));
    }

    @Test
    @DisplayName("registrar: si el animal no existe -> lanza IllegalArgumentException y no guarda")
    void registrar_animalInexistente() {
        when(animalRepo.findById(1L)).thenReturn(Optional.empty());
        EventoSanitarioRequest req = new EventoSanitarioRequest(
                EventoSanitario.Tipo.VACUNA, LocalDate.now(), "obs", 1L);

        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(req));
        verify(eventoRepo, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorAnimal: devuelve la lista de eventos mapeada")
    void obtenerPorAnimal_devuelveLista() {
        EventoSanitario e = new EventoSanitario();
        e.setId(5L);
        e.setTipo(EventoSanitario.Tipo.CASTRACION);
        e.setFecha(LocalDate.of(2026, 9, 15));
        e.setAnimal(animal(1L));
        when(eventoRepo.findByAnimalIdOrderByFechaDesc(1L)).thenReturn(List.of(e));

        List<EventoSanitarioResponse> lista = servicio.obtenerPorAnimal(1L);

        assertEquals(1, lista.size());
        assertEquals(EventoSanitario.Tipo.CASTRACION, lista.get(0).tipo());
    }

    @Test
    @DisplayName("eliminar: si el evento no existe -> lanza EntityNotFoundException y no borra")
    void eliminar_inexistente() {
        when(eventoRepo.existsById(7L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> servicio.eliminar(7L));
        verify(eventoRepo, never()).deleteById(any());
    }

    @Test
    @DisplayName("eliminar: si el evento existe -> lo borra")
    void eliminar_existente() {
        when(eventoRepo.existsById(7L)).thenReturn(true);

        servicio.eliminar(7L);

        verify(eventoRepo).deleteById(7L);
    }
}
