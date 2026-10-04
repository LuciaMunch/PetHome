package com.pethome.dtos.request;

import com.pethome.models.TipoVivienda;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SolicitudAdopcionRequest {

    @NotNull(message = "El tipo de vivienda es obligatorio")
    private TipoVivienda tipoVivienda;

    @NotNull(message = "Debe indicar si tiene patio")
    private Boolean tienePatio;

    @NotNull(message = "Debe indicar la cantidad de integrantes del hogar")
    @Min(value = 1, message = "Debe haber al menos 1 integrante")
    private Integer integrantesHogar;

    @NotNull(message = "Debe indicar si tiene otras mascotas")
    private Boolean otrasMascotas;
    private Integer cantidadOtrasMascotas;
    private String cualesOtrasMascotas;

    @NotNull(message = "Debe indicar si tiene experiencia previa")
    private Boolean experienciaPrevia;
    private String cualesMascotasActuales;

    private Boolean tieneTrabajo;
    private String cualTrabajo;

    private Boolean viajaSeguido;
    private String quienCuidaEnViajes;

    private String motivo;

    @NotNull(message = "El id del animal es obligatorio")
    private Long animalId;
}