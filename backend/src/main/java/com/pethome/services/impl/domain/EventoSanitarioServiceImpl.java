package com.pethome.services.impl.domain;

import com.pethome.models.Animal;
import com.pethome.models.EventoSanitario;
import com.pethome.repositories.AnimalRepository;
import com.pethome.repositories.EventoSanitarioRepository;
import com.pethome.services.interfaces.domain.EventoSanitarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoSanitarioServiceImpl implements EventoSanitarioService {

    private final EventoSanitarioRepository eventoSanitarioRepository;
    private final AnimalRepository animalRepository;

    @Override
    public EventoSanitario registrarEvento(EventoSanitario evento, Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new EntityNotFoundException("Animal no encontrado con id: " + animalId));
        evento.setAnimal(animal);
        return eventoSanitarioRepository.save(evento);
    }

    @Override
    public List<EventoSanitario> obtenerHistorialPorAnimal(Long animalId) {
        return eventoSanitarioRepository.findByAnimalIdOrderByFechaDesc(animalId);
    }
}