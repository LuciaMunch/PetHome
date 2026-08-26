package com.pethome.services.impl.domain;

import com.pethome.models.*;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.SolicitudAdopcionRepository;
import com.pethome.repositories.UserRepository;
import com.pethome.services.interfaces.domain.AnimalService;
import com.pethome.services.interfaces.domain.SolicitudAdopcionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudAdopcionServiceImpl implements SolicitudAdopcionService {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AnimalService animalService; // acá está el marcarAdoptado(id) de Lucía

    @Override
    public SolicitudAdopcion enviarSolicitud(SolicitudAdopcion solicitud, Long animalId, Long usuarioId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Animal no encontrado con id: " + animalId));

        User usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + usuarioId));

        if (animal.getEstado() != EstadoAnimal.DISPONIBLE) {
            throw new IllegalStateException("El animal no está disponible para adopción");
        }

        solicitudAdopcionRepository
                .findByAnimalIdAndUsuarioIdAndEstado(animalId, usuarioId, EstadoSolicitud.PENDIENTE)
                .ifPresent(s -> {
                    throw new IllegalStateException("Ya existe una solicitud pendiente para este animal");
                });

        solicitud.setAnimal(animal);
        solicitud.setUsuario(usuario);
        solicitud.setFecha(LocalDate.now());
        solicitud.setEstado(EstadoSolicitud.PENDIENTE);

        return solicitudAdopcionRepository.save(solicitud);
    }

    @Override
    public Page<SolicitudAdopcion> obtenerPendientes(Pageable pageable) {
        return solicitudAdopcionRepository.findByEstado(EstadoSolicitud.PENDIENTE, pageable);
    }

    @Override
    public List<SolicitudAdopcion> obtenerMisSolicitudes(Long usuarioId) {
        return solicitudAdopcionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public void aprobarSolicitud(Long solicitudId) {
        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(solicitudId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        Long animalId = solicitud.getAnimal().getId();

        // 1. Aprobar esta solicitud
        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitudAdopcionRepository.save(solicitud);

        // 2. Rechazar las demás pendientes del mismo animal
        List<SolicitudAdopcion> otrasPendientes =
                solicitudAdopcionRepository.findByAnimalIdAndEstado(animalId, EstadoSolicitud.PENDIENTE);
        otrasPendientes.forEach(s -> s.setEstado(EstadoSolicitud.RECHAZADA));
        solicitudAdopcionRepository.saveAll(otrasPendientes);

        // 3. Marcar el animal como adoptado (delegado al service de Lucía)
        animalService.marcarAdoptado(animalId);
    }

    @Override
    public void rechazarSolicitud(Long solicitudId) {
        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(solicitudId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        solicitudAdopcionRepository.save(solicitud);
    }
}