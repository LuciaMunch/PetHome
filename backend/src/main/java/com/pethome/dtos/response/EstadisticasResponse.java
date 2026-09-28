package com.pethome.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class EstadisticasResponse {

    private long totalAnimales;
    private long adoptados;
    private Map<String, Long> animalesPorEstado;
    private Map<String, Long> animalesPorEspecie;
    private Map<String, Long> animalesPorTamanio;
    private long solicitudesPendientes;
    private long solicitudesAprobadas;
    private long solicitudesRechazadas;
    private long totalAdoptantes;
    private Map<String, Long> adopcionesPorMes;
}