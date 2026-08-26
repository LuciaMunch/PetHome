package com.pethome.mappers;

import com.pethome.dtos.request.SolicitudAdopcionRequest;
import com.pethome.dtos.response.SolicitudAdopcionResponse;
import com.pethome.models.SolicitudAdopcion;
import org.springframework.stereotype.Component;

@Component
public class SolicitudAdopcionMapper {

    public SolicitudAdopcion toEntity(SolicitudAdopcionRequest request) {
        return SolicitudAdopcion.builder()
                .tipoVivienda(request.getTipoVivienda())
                .tienePatio(request.getTienePatio())
                .integrantesHogar(request.getIntegrantesHogar())
                .otrasMascotas(request.getOtrasMascotas())
                .experienciaPrevia(request.getExperienciaPrevia())
                .motivo(request.getMotivo())
                .build();
    }

    public SolicitudAdopcionResponse toResponse(SolicitudAdopcion solicitud) {
        return new SolicitudAdopcionResponse(
                solicitud.getId(),
                solicitud.getFecha(),
                solicitud.getEstado(),
                solicitud.getTipoVivienda(),
                solicitud.isTienePatio(),
                solicitud.getIntegrantesHogar(),
                solicitud.isOtrasMascotas(),
                solicitud.isExperienciaPrevia(),
                solicitud.getMotivo(),
                solicitud.getAnimal().getId(),
                solicitud.getUsuario().getId()
        );
    }
}