package com.pethome.repositories;
import com.pethome.models.Role;
import com.pethome.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNombreUsuario(String nombre_usuario);
    long countByRol(Role rol);
    List<User> findByRolNot(Role rol);
}