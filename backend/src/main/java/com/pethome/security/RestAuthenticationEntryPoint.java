package com.pethome.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Se dispara cuando una petición a un recurso protegido NO está autenticada:
 * no se envió token, o el token es inválido / está vencido.
 * Es un problema de AUTENTICACIÓN  =>  responde 401 (Unauthorized).
 *
 * Antes de agregar esta clase, Spring Security respondía 403 por defecto en este
 * caso (Http403ForbiddenEntryPoint), lo que impedía distinguir "no estás logueado"
 * de "no tenés permiso".
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Se requiere un token de autenticación válido");
        problema.setTitle("No autenticado");

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        mapper.writeValue(response.getWriter(), problema);
    }
}
