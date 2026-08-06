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
