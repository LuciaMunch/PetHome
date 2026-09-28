package com.pethome.controllers.get;

import com.pethome.dtos.response.UsuarioResponse;
import com.pethome.mappers.UsuarioMapper;
import com.pethome.models.Role;
import com.pethome.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioGetController {

    private final UserRepository userRepository;
    private final UsuarioMapper usuarioMapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponse> listarAdoptantes() {
        return userRepository.findByRolNot(Role.ADMIN)
                .stream()
                .map(usuarioMapper::toResponse)
                .toList();
    }
}