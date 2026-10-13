package com.pethome.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail manejarEntidadNoEncontrada(EntityNotFoundException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Recurso no encontrado");
        return problema;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail manejarEstadoInvalido(IllegalStateException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problema.setTitle("Operación no permitida");
        return problema;
    }

    /*
     * Concurrencia: cuando dos transacciones modifican el mismo animal a la vez,
     * el control de versiones optimista (@Version) hace fallar a la segunda con
     * esta excepción. La traducimos a 409 Conflict con un mensaje claro en vez de 500.
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail manejarConflictoDeConcurrencia(ObjectOptimisticLockingFailureException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "El recurso fue modificado por otra operación simultánea. Volvé a intentar.");
        problema.setTitle("Conflicto de concurrencia");
        return problema;
    }

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
        log.error("Error inesperado no manejado", ex);
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado");
        problema.setTitle("Error interno");
        return problema;
    }
}