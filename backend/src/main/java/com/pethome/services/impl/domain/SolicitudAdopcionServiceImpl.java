package com.pethome.services.impl.domain;

import com.pethome.dtos.response.SolicitudAdopcionResponse;
import com.pethome.mappers.SolicitudAdopcionMapper;
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
    private final AnimalService animalService;
    private final SolicitudAdopcionMapper mapper;

    @Override
    @Transactional
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

        SolicitudAdopcion guardada = solicitudAdopcionRepository.save(solicitud);

        // Al entrar una solicitud, el animal pasa a EN_PROCESO
        animalService.marcarEnProceso(animalId);

        return guardada;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SolicitudAdopcionResponse> obtenerPendientes(Pageable pageable) {
        Page<SolicitudAdopcion> solicitudes =
                solicitudAdopcionRepository.findByEstado(EstadoSolicitud.PENDIENTE, pageable);
        return solicitudes.map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SolicitudAdopcionResponse> obtenerMisSolicitudes(Long usuarioId) {
        return solicitudAdopcionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void aprobarSolicitud(Long solicitudId) {
        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(solicitudId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        // --- Guardas de negocio (control de concurrencia, primera línea de defensa) ---
        // 1) La solicitud debe seguir PENDIENTE. Si otro admin ya la aprobó/rechazó, cortamos acá.
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException(
                    "La solicitud ya fue procesada (estado actual: " + solicitud.getEstado() + ")");
        }

        Animal animal = solicitud.getAnimal();

        // 2) El animal no puede estar ya adoptado por otra solicitud.
        if (animal.getEstado() == EstadoAnimal.ADOPTADO) {
            throw new IllegalStateException("El animal ya fue adoptado por otra solicitud");
        }

        Long animalId = animal.getId();

        solicitud.setEstado(EstadoSolicitud.APROBADA);
        solicitudAdopcionRepository.save(solicitud);

        // Rechaza automáticamente las demás solicitudes pendientes del mismo animal
        List<SolicitudAdopcion> otrasPendientes =
                solicitudAdopcionRepository.findByAnimalIdAndEstado(animalId, EstadoSolicitud.PENDIENTE);
        otrasPendientes.forEach(s -> s.setEstado(EstadoSolicitud.RECHAZADA));
        solicitudAdopcionRepository.saveAll(otrasPendientes);

        // Marca el animal como ADOPTADO. Si dos transacciones llegan hasta acá en paralelo,
        // el @Version de Animal hace que la segunda falle con ObjectOptimisticLockingFailureException
        // (segunda línea de defensa, a nivel de base de datos).
        animalService.marcarAdoptado(animalId);
    }

    @Override
    @Transactional
    public void rechazarSolicitud(Long solicitudId) {
        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(solicitudId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con id: " + solicitudId));

        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new IllegalStateException(
                    "La solicitud ya fue procesada (estado actual: " + solicitud.getEstado() + ")");
        }

        solicitud.setEstado(EstadoSolicitud.RECHAZADA);
        solicitudAdopcionRepository.save(solicitud);

        // Al rechazar, el animal vuelve a estar disponible
        animalService.marcarDisponible(solicitud.getAnimal().getId());
    }
}