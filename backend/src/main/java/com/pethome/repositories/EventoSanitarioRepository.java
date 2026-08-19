package com.pethome.repositories;

import com.pethome.models.EventoSanitario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoSanitarioRepository extends JpaRepository<EventoSanitario, Long> {

    List<EventoSanitario> findByAnimalIdOrderByFechaDesc(Long animalId);
}