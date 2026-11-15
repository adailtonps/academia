package com.academia.repository;

import com.academia.domain.Assinatura;
import com.academia.enums.StatusAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AssinaturaRepository extends JpaRepository<Assinatura, Integer> {
    boolean existsByUsuario_IdAndStatusAssinatura(Long idUsuario, StatusAssinatura statusAssinatura);
    List<Assinatura> findByStatusAssinatura(StatusAssinatura statusAssinatura);
    List<Assinatura> findByStatusAssinaturaAndDataLimitePagamentoBefore(StatusAssinatura statusAssinatura, LocalDateTime  dataLimitePagamento);
}
