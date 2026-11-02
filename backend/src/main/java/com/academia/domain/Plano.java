package com.academia.domain;

import com.academia.enums.StatusPlano;
import com.academia.enums.TipoPlano;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    private int id;

    private String nomePlano;
    private String descricao;
    private BigDecimal valorPlano;

    @Enumerated(EnumType.STRING)
    private TipoPlano tipoPlano;

    private LocalDateTime duracaoEmDias;

    @Enumerated(EnumType.STRING)
    private StatusPlano statusPlano;

    @OneToMany
    private Assinatura assinatura;
}
