package com.pethome.repositories;

import com.pethome.models.Animal;
import com.pethome.models.Especie;
import com.pethome.models.EstadoAnimal;
import com.pethome.models.Sexo;
import com.pethome.models.Tamanio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("""
            SELECT a FROM Animal a
            WHERE a.estado = :estado
              AND (:especie IS NULL OR a.especie = :especie)
              AND (:sexo IS NULL OR a.sexo = :sexo)
              AND (:tamanio IS NULL OR a.tamanio = :tamanio)
              AND (:edadMax IS NULL OR a.edad <= :edadMax)
            """)
    Page<Animal> buscarCatalogo(@Param("estado") EstadoAnimal estado,
                                @Param("especie") Especie especie,
                                @Param("sexo") Sexo sexo,
                                @Param("tamanio") Tamanio tamanio,
                                @Param("edadMax") Integer edadMax,
                                Pageable pageable);
}
