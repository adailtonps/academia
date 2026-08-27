package com.academia.repository;

import com.academia.domain.Checkin;
import com.academia.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Optional<Checkin> findByUsuarioAndCheckoutIsNull(Usuario usuario);
    Optional<Checkin> findTopByUsuarioOrderByCheckinDesc(Usuario usuario);
    List<Checkin> findByUsuarioOrderByCheckinDesc(Usuario usuario);
    @Transactional
    void deleteByUsuario_id(Long id);
}
