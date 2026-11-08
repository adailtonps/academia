package com.academia.dto;

import com.academia.domain.Assinatura;
import com.academia.domain.Usuario;
import com.academia.enums.StatusPlano;
import com.academia.enums.TipoPlano;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CadastroPlanoDto {
    @NotBlank(message = "Nome do plano obrigatório!")
    private String nomePlano;

    @NotBlank(message = "Descrição do plano obrigatória!")
    private String descricao;

    @NotBlank(message = "Valor do plano obrigatório!")
    private BigDecimal valorPlano;

    @NotBlank(message = "Tipo do plano obrigatório!")
    private TipoPlano tipoPlano;

    @NotBlank(message = "Digite se o plano vai estar ativo ou inativo!")
    private StatusPlano statusPlano;

    @NotBlank(message = "Duração do plano em dias obrigatório!")
    private Integer duracaoEmDias;

    @NotBlank(message = "Informe se o plano inclui personal!")
    private boolean possuiPersonal;

}
