package com.pethome.configs;

import com.pethome.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // Define la cadena de seguridad principal de la app
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        return http
                // Como usamos JWT (stateless), NO usamos cookies ni CSRF => lo desactivamos
                .csrf(AbstractHttpConfigurer::disable)

                // Sin sesiones: cada request es independiente (no guardamos estado en el server)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Reglas de autorización (se evalúan de arriba a abajo, primera coincidencia gana):
                .authorizeHttpRequests(auth -> auth
                        // Cualquiera puede registrarse/loguearse
                        .requestMatchers("/api/auth/**").permitAll()
                        // Solo usuarios con rol ADMIN entran a /api/admin/**
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // TODO lo demás exige estar autenticado con token válido
                        .anyRequest().authenticated())

                // Registra NUESTRO filtro ANTES del filtro estándar de login de Spring.
                // Así primero validamos el JWT y después Spring decide si deja pasar.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Cifra las contraseñas (hash BCrypt): nunca guardamos la contraseña en texto plano
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
