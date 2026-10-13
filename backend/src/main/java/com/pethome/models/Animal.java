package com.pethome.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "animal")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Control de concurrencia optimista (optimistic locking).
     * Hibernate incrementa automáticamente esta columna en cada UPDATE y verifica,
     * al guardar, que la versión no haya cambiado desde que se leyó la fila.
     * Si dos transacciones intentan modificar el MISMO animal a la vez (por ejemplo,
     * dos administradores aprobando solicitudes del mismo animal), la segunda en
     * confirmar falla con ObjectOptimisticLockingFailureException en lugar de pisar
     * el cambio de la primera. Así nunca quedan dos adopciones aprobadas para un mismo animal.
     */
    @Version
    @Column(nullable = false)
    private Long version;

    @Column(nullable = false, length = 120)
    private String nombre;
    private Integer edad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Especie especie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tamanio tamanio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoAnimal estado;
}
