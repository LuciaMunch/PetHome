package com.pethome.repositories;

import com.pethome.models.EstadoSolicitud;
import com.pethome.models.SolicitudAdopcion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {

    // HU-11: el admin ve las solicitudes pendientes, paginado
    Page<SolicitudAdopcion> findByEstado(EstadoSolicitud estado, Pageable pageable);

    // HU-12: el adoptante ve solo sus propias solicitudes
    List<SolicitudAdopcion> findByUsuarioId(Long usuarioId);

    // Para la regla de "no duplicar solicitud pendiente sobre el mismo animal" (HU-10)
    Optional<SolicitudAdopcion> findByAnimalIdAndUsuarioIdAndEstado(
            Long animalId, Long usuarioId, EstadoSolicitud estado);

    // Para HU-11: al aprobar una, hay que rechazar las demás pendientes del mismo animal
    List<SolicitudAdopcion> findByAnimalIdAndEstado(Long animalId, EstadoSolicitud estado);
}