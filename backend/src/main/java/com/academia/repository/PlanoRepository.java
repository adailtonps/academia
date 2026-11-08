package com.academia.repository;

import com.academia.domain.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {
    boolean existsByNomePlanoAndValorPlanoAndDuracaoEmDiasAndPossuiPersonal(String nomePlano, BigDecimal valorPlano,
                                                                            Integer duracaoEmDias, boolean possuiPersonal);
}
