package com.pethome.dtos.response;

import com.pethome.models.EstadoSolicitud;
import com.pethome.models.TipoVivienda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
public class SolicitudAdopcionResponse {

    private Long id;
    private LocalDate fecha;
    private EstadoSolicitud estado;
    private TipoVivienda tipoVivienda;
    private boolean tienePatio;
    private int integrantesHogar;
    private boolean otrasMascotas;
    private boolean experienciaPrevia;
    private String motivo;
    private Long animalId;
    private Long usuarioId;
}