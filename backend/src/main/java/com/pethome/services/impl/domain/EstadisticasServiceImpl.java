package com.pethome.services.impl.domain;

import com.pethome.dtos.response.EstadisticasResponse;
import com.pethome.models.*;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.SolicitudAdopcionRepository;
import com.pethome.repositories.UserRepository;
import com.pethome.services.interfaces.domain.EstadisticasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EstadisticasServiceImpl implements EstadisticasService {

    private final AnimalRepository animalRepository;
    private final SolicitudAdopcionRepository solicitudRepository;
    private final UserRepository userRepository;

    private Map<String, Long> contarPorLista(Enum<?>[] valores, java.util.function.Function<Enum<?>, Long> contador) {
        Map<String, Long> mapa = new LinkedHashMap<>();
        for (Enum<?> valor : valores) {
            mapa.put(valor.name(), contador.apply(valor));
        }
        return mapa;
    }

    @Override
    public EstadisticasResponse obtenerResumen() {
        Map<String, Long> porEstado = contarPorLista(EstadoAnimal.values(), e -> animalRepository.countByEstado((EstadoAnimal) e));
        Map<String, Long> porEspecie = contarPorLista(Especie.values(), e -> animalRepository.countByEspecie((Especie) e));
        Map<String, Long> porTamanio = contarPorLista(Tamanio.values(), e -> animalRepository.countByTamanio((Tamanio) e));

        Map<String, Long> adopcionesPorMes = new LinkedHashMap<>();
        for (Object[] fila : solicitudRepository.contarAprobadasPorMes(EstadoSolicitud.APROBADA)) {
            String anio = String.valueOf(fila[0]);
            int mes = ((Number) fila[1]).intValue();
            adopcionesPorMes.put(anio + "-" + String.format("%02d", mes), (Long) fila[2]);
        }

        return EstadisticasResponse.builder()
                .totalAnimales(animalRepository.count())
                .adoptados(porEstado.getOrDefault("ADOPTADO", 0L))
                .animalesPorEstado(porEstado)
                .animalesPorEspecie(porEspecie)
                .animalesPorTamanio(porTamanio)
                .solicitudesPendientes(solicitudRepository.countByEstado(EstadoSolicitud.PENDIENTE))
                .solicitudesAprobadas(solicitudRepository.countByEstado(EstadoSolicitud.APROBADA))
                .solicitudesRechazadas(solicitudRepository.countByEstado(EstadoSolicitud.RECHAZADA))
                .totalAdoptantes(userRepository.countByRol(Role.ADOPTANTE))
                .adopcionesPorMes(adopcionesPorMes)
                .build();
    }
}
