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
                .cantidadOtrasMascotas(request.getCantidadOtrasMascotas())
                .cualesOtrasMascotas(request.getCualesOtrasMascotas())
                .experienciaPrevia(request.getExperienciaPrevia())
                .cualesMascotasActuales(request.getCualesMascotasActuales())
                .tieneTrabajo(request.getTieneTrabajo())
                .cualTrabajo(request.getCualTrabajo())
                .viajaSeguido(request.getViajaSeguido())
                .quienCuidaEnViajes(request.getQuienCuidaEnViajes())
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
                solicitud.getAnimal().getNombre(),
                solicitud.getUsuario().getId(),
                solicitud.getUsuario().getNombreUsuario(),
                solicitud.getUsuario().getEmail(),
                solicitud.getUsuario().getNombreCompleto(),
                solicitud.getUsuario().getTelefono(),
                solicitud.getUsuario().getDireccion(),
                solicitud.getUsuario().getCiudad(),
                solicitud.getUsuario().getProvincia(),
                solicitud.getCantidadOtrasMascotas(),
                solicitud.getCualesOtrasMascotas(),
                solicitud.getCualesMascotasActuales(),
                solicitud.getTieneTrabajo(),
                solicitud.getCualTrabajo(),
                solicitud.getViajaSeguido(),
                solicitud.getQuienCuidaEnViajes()
        );
    }
}