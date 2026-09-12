package com.pethome.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Cuando no se encuentra una entidad (animal, foto, solicitud, usuario, etc.) → 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail manejarEntidadNoEncontrada(EntityNotFoundException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Recurso no encontrado");
        return problema;
    }

    // Cuando se viola una regla de negocio (ej: animal no disponible, solicitud duplicada) → 409
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail manejarEstadoInvalido(IllegalStateException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problema.setTitle("Operación no permitida");
        return problema;
    }

    // Cuando fallan las validaciones de un DTO (@NotBlank, @NotNull, etc.) → 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail manejarValidacion(MethodArgumentNotValidException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Hay errores de validación en los datos enviados");
        problema.setTitle("Datos inválidos");

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage()));
        problema.setProperty("errores", errores);

        return problema;
    }

    // Cualquier otro error no previsto → 500
    @ExceptionHandler(Exception.class)
    public ProblemDetail manejarErrorGeneral(Exception ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado");
        problema.setTitle("Error interno");
        return problema;
    }
}
