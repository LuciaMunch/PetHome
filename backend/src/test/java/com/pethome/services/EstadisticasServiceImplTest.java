package com.pethome.services;

import com.pethome.dtos.response.EstadisticasResponse;
import com.pethome.models.*;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.SolicitudAdopcionRepository;
import com.pethome.repositories.UserRepository;
import com.pethome.services.impl.domain.EstadisticasServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Pruebas UNITARIAS del módulo de Métricas / Panel administrativo.
 * Se mockean los conteos de cada repositorio y se verifica que el resumen
 * arme los totales correctamente.
 */
@ExtendWith(MockitoExtension.class)
class EstadisticasServiceImplTest {

    @Mock private AnimalRepository animalRepository;
    @Mock private SolicitudAdopcionRepository solicitudRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private EstadisticasServiceImpl servicio;

    @Test
    @DisplayName("obtenerResumen: arma los totales a partir de los conteos de los repositorios")
    void obtenerResumen_armaTotales() {
        // Conteos de animales por estado
        when(animalRepository.countByEstado(EstadoAnimal.DISPONIBLE)).thenReturn(13L);
        when(animalRepository.countByEstado(EstadoAnimal.EN_PROCESO)).thenReturn(2L);
        when(animalRepository.countByEstado(EstadoAnimal.ADOPTADO)).thenReturn(4L);
        // Por especie
        when(animalRepository.countByEspecie(Especie.PERRO)).thenReturn(14L);
        when(animalRepository.countByEspecie(Especie.GATO)).thenReturn(5L);
        // Por tamaño
        when(animalRepository.countByTamanio(Tamanio.PEQUENIO)).thenReturn(12L);
        when(animalRepository.countByTamanio(Tamanio.MEDIANO)).thenReturn(4L);
        when(animalRepository.countByTamanio(Tamanio.GRANDE)).thenReturn(3L);
        // Total y adopciones por mes
        when(animalRepository.count()).thenReturn(19L);
        when(solicitudRepository.contarAprobadasPorMes(EstadoSolicitud.APROBADA))
                .thenReturn(List.of());
        // Solicitudes por estado
        when(solicitudRepository.countByEstado(EstadoSolicitud.PENDIENTE)).thenReturn(2L);
        when(solicitudRepository.countByEstado(EstadoSolicitud.APROBADA)).thenReturn(4L);
        when(solicitudRepository.countByEstado(EstadoSolicitud.RECHAZADA)).thenReturn(6L);
        // Adoptantes
        when(userRepository.countByRol(Role.ADOPTANTE)).thenReturn(3L);

        EstadisticasResponse r = servicio.obtenerResumen();

        assertEquals(19L, r.getTotalAnimales());
        assertEquals(4L, r.getAdoptados());                       // = animales en estado ADOPTADO
        assertEquals(2L, r.getSolicitudesPendientes());
        assertEquals(4L, r.getSolicitudesAprobadas());
        assertEquals(6L, r.getSolicitudesRechazadas());
        assertEquals(3L, r.getTotalAdoptantes());
        assertEquals(13L, r.getAnimalesPorEstado().get("DISPONIBLE"));
        assertEquals(14L, r.getAnimalesPorEspecie().get("PERRO"));
    }
}
