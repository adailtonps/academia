package com.academia.dto;

import com.academia.enums.StatusPlano;
import com.academia.enums.TipoPlano;

import java.math.BigDecimal;

public record CadastroPlanoResponseDto(
        String nomePlano,
        String descricao,
        BigDecimal valorPlano,
        TipoPlano tipoPlano,
        StatusPlano statusPlano,
        Integer duracao,
        boolean possuiPersonal
        )
    {

    }
