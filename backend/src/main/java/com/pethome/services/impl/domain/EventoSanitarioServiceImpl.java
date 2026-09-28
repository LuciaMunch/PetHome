package com.pethome.services.impl.domain;

import com.pethome.dtos.request.EventoSanitarioRequest;
import com.pethome.dtos.response.EventoSanitarioResponse;
import com.pethome.models.Animal;
import com.pethome.models.EventoSanitario;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.EventoSanitarioRepository;
import com.pethome.services.interfaces.domain.EventoSanitarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoSanitarioServiceImpl implements EventoSanitarioService {

    private final EventoSanitarioRepository eventoRepo;
    private final AnimalRepository animalRepo;

    public EventoSanitarioServiceImpl(EventoSanitarioRepository eventoRepo, AnimalRepository animalRepo) {
        this.eventoRepo = eventoRepo;
        this.animalRepo = animalRepo;
    }

    @Override
    public List<EventoSanitarioResponse> obtenerPorAnimal(Long animalId) {
        return eventoRepo.findByAnimalIdOrderByFechaDesc(animalId)
                .stream()
                .map(EventoSanitarioResponse::fromEntity)
                .toList();
    }

    @Override
    public EventoSanitarioResponse registrar(EventoSanitarioRequest req) {
        Animal animal = animalRepo.findById(req.animalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal no encontrado: " + req.animalId()));

        EventoSanitario evento = new EventoSanitario();
        evento.setTipo(req.tipo());
        evento.setFecha(req.fecha());
        evento.setObservaciones(req.observaciones());
        evento.setAnimal(animal);

        return EventoSanitarioResponse.fromEntity(eventoRepo.save(evento));
    }

    @Override
    public void eliminar(Long id) {
        if (!eventoRepo.existsById(id)) {
            throw new EntityNotFoundException("Evento sanitario no encontrado con id: " + id);
        }
        eventoRepo.deleteById(id);
    }
}