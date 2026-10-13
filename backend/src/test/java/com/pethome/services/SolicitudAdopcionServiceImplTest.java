package com.pethome.services;

import com.pethome.mappers.SolicitudAdopcionMapper;
import com.pethome.models.*;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.SolicitudAdopcionRepository;
import com.pethome.repositories.UserRepository;
import com.pethome.services.impl.domain.SolicitudAdopcionServiceImpl;
import com.pethome.services.interfaces.domain.AnimalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas UNITARIAS del servicio de adopciones.
 *
 * "Unitaria" = prueba una sola clase (SolicitudAdopcionServiceImpl) de forma aislada.
 * No levanta Spring ni toca la base de datos: los colaboradores (repositorios,
 * AnimalService, mapper) se reemplazan por "mocks" (dobles de prueba) con Mockito.
 *
 * Patrón de cada test (AAA):
 *   1. PREPARAR (Arrange): armo los datos y defino qué devuelven los mocks.
 *   2. EJECUTAR (Act): llamo al método que quiero probar.
 *   3. VERIFICAR (Assert): compruebo el resultado y/o que se llamó a lo que esperaba.
 */
@ExtendWith(MockitoExtension.class)
class SolicitudAdopcionServiceImplTest {

    // @Mock crea un doble de prueba de cada dependencia
    @Mock private SolicitudAdopcionRepository solicitudRepository;
    @Mock private AnimalRepository animalRepository;
    @Mock private UserRepository userRepository;
    @Mock private AnimalService animalService;
    @Mock private SolicitudAdopcionMapper mapper;

    // @InjectMocks crea el servicio real e inyecta los mocks de arriba
    @InjectMocks private SolicitudAdopcionServiceImpl servicio;

    // ---------- Métodos auxiliares para armar datos de prueba ----------

    private Animal animalConEstado(Long id, EstadoAnimal estado) {
        return Animal.builder()
                .id(id)
                .nombre("Firulais")
                .especie(Especie.PERRO)
                .sexo(Sexo.MACHO)
                .tamanio(Tamanio.MEDIANO)
                .estado(estado)
                .build();
    }

    private SolicitudAdopcion solicitudConEstado(Long id, Animal animal, EstadoSolicitud estado) {
        return SolicitudAdopcion.builder()
                .id(id)
                .animal(animal)
                .estado(estado)
                .build();
    }

    // =====================================================================
    //  enviarSolicitud
    // =====================================================================

    @Test
    @DisplayName("enviarSolicitud: animal disponible y sin solicitud previa -> guarda y marca EN_PROCESO")
    void enviarSolicitud_caminoFeliz() {
        // PREPARAR
        Animal animal = animalConEstado(1L, EstadoAnimal.DISPONIBLE);
        User usuario = User.builder().id(10L).build();
        SolicitudAdopcion nueva = SolicitudAdopcion.builder().build();

        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(userRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(solicitudRepository.findByAnimalIdAndUsuarioIdAndEstado(1L, 10L, EstadoSolicitud.PENDIENTE))
                .thenReturn(Optional.empty());
        when(solicitudRepository.save(any(SolicitudAdopcion.class))).thenReturn(nueva);

        // EJECUTAR
        servicio.enviarSolicitud(nueva, 1L, 10L);

        // VERIFICAR
        assertEquals(EstadoSolicitud.PENDIENTE, nueva.getEstado());   // quedó PENDIENTE
        verify(solicitudRepository).save(nueva);                      // se guardó
        verify(animalService).marcarEnProceso(1L);                    // el animal pasó a EN_PROCESO
    }

    @Test
    @DisplayName("enviarSolicitud: si el animal no está disponible -> lanza IllegalStateException")
    void enviarSolicitud_animalNoDisponible() {
        Animal animal = animalConEstado(1L, EstadoAnimal.ADOPTADO);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(userRepository.findById(10L)).thenReturn(Optional.of(User.builder().id(10L).build()));

        assertThrows(IllegalStateException.class,
                () -> servicio.enviarSolicitud(SolicitudAdopcion.builder().build(), 1L, 10L));

        // No debe tocar el estado del animal
        verify(animalService, never()).marcarEnProceso(anyLong());
    }

    @Test
    @DisplayName("enviarSolicitud: si ya existe una solicitud pendiente -> lanza IllegalStateException")
    void enviarSolicitud_yaExistePendiente() {
        Animal animal = animalConEstado(1L, EstadoAnimal.DISPONIBLE);
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(userRepository.findById(10L)).thenReturn(Optional.of(User.builder().id(10L).build()));
        when(solicitudRepository.findByAnimalIdAndUsuarioIdAndEstado(1L, 10L, EstadoSolicitud.PENDIENTE))
                .thenReturn(Optional.of(SolicitudAdopcion.builder().build()));

        assertThrows(IllegalStateException.class,
                () -> servicio.enviarSolicitud(SolicitudAdopcion.builder().build(), 1L, 10L));

        verify(solicitudRepository, never()).save(any());
    }

    // =====================================================================
    //  aprobarSolicitud  (incluye las guardas de concurrencia)
    // =====================================================================

    @Test
    @DisplayName("aprobarSolicitud: camino feliz -> aprueba, rechaza las otras pendientes y marca ADOPTADO")
    void aprobarSolicitud_caminoFeliz() {
        Animal animal = animalConEstado(1L, EstadoAnimal.EN_PROCESO);
        SolicitudAdopcion solicitud = solicitudConEstado(100L, animal, EstadoSolicitud.PENDIENTE);
        SolicitudAdopcion otraPendiente = solicitudConEstado(101L, animal, EstadoSolicitud.PENDIENTE);

        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));
        when(solicitudRepository.findByAnimalIdAndEstado(1L, EstadoSolicitud.PENDIENTE))
                .thenReturn(List.of(otraPendiente));

        servicio.aprobarSolicitud(100L);

        assertEquals(EstadoSolicitud.APROBADA, solicitud.getEstado());      // la aprobada
        assertEquals(EstadoSolicitud.RECHAZADA, otraPendiente.getEstado()); // la otra se rechazó
        verify(animalService).marcarAdoptado(1L);                           // el animal quedó ADOPTADO
    }

    @Test
    @DisplayName("aprobarSolicitud: si la solicitud ya no está PENDIENTE -> lanza IllegalStateException (concurrencia)")
    void aprobarSolicitud_solicitudYaProcesada() {
        Animal animal = animalConEstado(1L, EstadoAnimal.EN_PROCESO);
        // Otro admin ya la aprobó: llega en estado APROBADA
        SolicitudAdopcion solicitud = solicitudConEstado(100L, animal, EstadoSolicitud.APROBADA);
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));

        assertThrows(IllegalStateException.class, () -> servicio.aprobarSolicitud(100L));

        // No debe volver a marcar el animal ni guardar
        verify(animalService, never()).marcarAdoptado(anyLong());
    }

    @Test
    @DisplayName("aprobarSolicitud: si el animal ya está ADOPTADO -> lanza IllegalStateException (concurrencia)")
    void aprobarSolicitud_animalYaAdoptado() {
        Animal animal = animalConEstado(1L, EstadoAnimal.ADOPTADO);
        SolicitudAdopcion solicitud = solicitudConEstado(100L, animal, EstadoSolicitud.PENDIENTE);
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));

        assertThrows(IllegalStateException.class, () -> servicio.aprobarSolicitud(100L));

        verify(animalService, never()).marcarAdoptado(anyLong());
    }

    @Test
    @DisplayName("aprobarSolicitud: si la solicitud no existe -> lanza EntityNotFoundException")
    void aprobarSolicitud_noExiste() {
        when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> servicio.aprobarSolicitud(999L));
    }

    // =====================================================================
    //  rechazarSolicitud
    // =====================================================================

    @Test
    @DisplayName("rechazarSolicitud: camino feliz -> rechaza y el animal vuelve a DISPONIBLE")
    void rechazarSolicitud_caminoFeliz() {
        Animal animal = animalConEstado(1L, EstadoAnimal.EN_PROCESO);
        SolicitudAdopcion solicitud = solicitudConEstado(100L, animal, EstadoSolicitud.PENDIENTE);
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));

        servicio.rechazarSolicitud(100L);

        assertEquals(EstadoSolicitud.RECHAZADA, solicitud.getEstado());
        verify(animalService).marcarDisponible(1L);
    }
}
