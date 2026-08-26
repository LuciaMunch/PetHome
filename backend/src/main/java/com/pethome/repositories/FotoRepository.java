package com.pethome.repositories;

import com.pethome.models.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {

    List<Foto> findByAnimalId(Long animalId);
}
