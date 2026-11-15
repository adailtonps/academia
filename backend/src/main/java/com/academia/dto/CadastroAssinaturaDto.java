package com.academia.dto;

import com.academia.domain.Plano;
import com.academia.domain.Usuario;
import com.academia.enums.StatusAssinatura;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CadastroAssinaturaDto {
    private Usuario usuario;
    private Plano plano;
    private LocalDateTime dataInicio;
    private LocalDateTime dataVencimento;
    private StatusAssinatura statusAssinatura;

}
