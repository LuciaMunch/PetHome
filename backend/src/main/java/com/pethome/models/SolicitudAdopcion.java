package com.pethome.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "solicitud_adopcion")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitudAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vivienda", nullable = false)
    private TipoVivienda tipoVivienda;

    @Column(name = "tiene_patio", nullable = false)
    private boolean tienePatio;

    @Column(name = "integrantes_hogar", nullable = false)
    private int integrantesHogar;

    @Column(name = "otras_mascotas", nullable = false)
    private boolean otrasMascotas;

    @Column(name = "experiencia_previa", nullable = false)
    private boolean experienciaPrevia;

    @Column(columnDefinition = "TEXT")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;
}