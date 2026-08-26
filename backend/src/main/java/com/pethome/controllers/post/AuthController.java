package com.pethome.controllers.post;


import com.pethome.dtos.request.RegisterRequest;
import com.pethome.dtos.request.LoginRequest;
import com.pethome.dtos.response.LoginResponse;
import com.pethome.models.Role;
import com.pethome.models.User;
import com.pethome.repositories.UserRepository;
import com.pethome.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.findByNombreUsuario(request.getNombre_usuario()).isPresent()) {
            return ResponseEntity.badRequest().body("Ese nombre de usuario ya existe");
        }

        User user = User.builder()
                .nombreUsuario(request.getNombre_usuario())
                .contraseña(passwordEncoder.encode(request.getContraseña()))
                .email(request.getEmail())
                .rol(request.getRol() != null ? request.getRol() : Role.ADOPTANTE)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(new LoginResponse(
                jwtService.generateToken(user), user.getNombreUsuario(), user.getRol()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByNombreUsuario(request.getNombre_usuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getContraseña(), user.getContraseña())) {
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        }

        return ResponseEntity.ok(new LoginResponse(
                jwtService.generateToken(user), user.getNombreUsuario(), user.getRol()));
    }
}