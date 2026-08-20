package com.pethome.services.interfaces.domain;

import com.pethome.models.SolicitudAdopcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SolicitudAdopcionService {

    SolicitudAdopcion enviarSolicitud(SolicitudAdopcion solicitud, Long animalId, Long usuarioId);

    Page<SolicitudAdopcion> obtenerPendientes(Pageable pageable);

    List<SolicitudAdopcion> obtenerMisSolicitudes(Long usuarioId);

    void aprobarSolicitud(Long solicitudId);

    void rechazarSolicitud(Long solicitudId);
}