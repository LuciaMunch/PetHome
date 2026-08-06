package com.pethome.dtos.response;

import com.pethome.models.Especie;
import com.pethome.models.EstadoAnimal;
import com.pethome.models.Sexo;
import com.pethome.models.Tamanio;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimalResponse {

    private Long id;
    private String nombre;
    private Especie especie;
    private Sexo sexo;
    private Tamanio tamanio;
    private Integer edad;
    private String descripcion;
    private EstadoAnimal estado;
}
