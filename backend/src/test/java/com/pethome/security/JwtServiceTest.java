package com.pethome.security;

import com.pethome.models.Role;
import com.pethome.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas UNITARIAS del servicio de JWT (módulo de Autenticación).
 * No necesita Spring: se construye el servicio a mano y se inyectan la clave y
 * la expiración con ReflectionTestUtils (reemplaza lo que haría @Value en producción).
 */
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // La clave debe tener al menos 32 caracteres (256 bits) para HMAC-SHA256
        ReflectionTestUtils.setField(jwtService, "secret",
                "clave-secreta-de-prueba-con-mas-de-32-caracteres");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3_600_000L); // 1 hora
    }

    private User usuario(String nombre) {
        return User.builder().id(1L).nombreUsuario(nombre).rol(Role.ADMIN).build();
    }

    @Test
    @DisplayName("generateToken + extractUsername: el token conserva el nombre de usuario")
    void generaYExtraeUsername() {
        User user = usuario("lucia");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("lucia", jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("isValid: un token recién generado es válido para su propio usuario")
    void isValid_tokenValido() {
        User user = usuario("lucia");
        String token = jwtService.generateToken(user);

        assertTrue(jwtService.isValid(token, user));
    }

    @Test
    @DisplayName("isValid: un token NO es válido para un usuario distinto")
    void isValid_otroUsuario() {
        String token = jwtService.generateToken(usuario("lucia"));

        assertFalse(jwtService.isValid(token, usuario("otro")));
    }
}
