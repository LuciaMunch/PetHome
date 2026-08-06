package com.pethome.dtos.request;

import com.pethome.models.Especie;
import com.pethome.models.Sexo;
import com.pethome.models.Tamanio;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
    private String nombre;

    @NotNull(message = "La especie es obligatoria")
    private Especie especie;

    @NotNull(message = "El sexo es obligatorio")
    private Sexo sexo;

    @NotNull(message = "El tamaño es obligatorio")
    private Tamanio tamanio;

    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer edad;

    private String descripcion;
}
