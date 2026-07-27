package com.academia.repository;

import com.academia.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByMatricula(String matricula);
    boolean existsByEmail(String email);

    boolean findByMatricula(String matricula);
    Optional<Usuario> findByEmail(String email);
}
