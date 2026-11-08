package com.academia.domain;

import com.academia.enums.StatusPlano;
import com.academia.enums.TipoPlano;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plano")
    private Long idPlano;

    @ManyToOne
    @JoinColumn(name = "id_admin")
    private Usuario admin;

    @Column(unique = true)
    private String nomePlano;

    private String descricao;
    private BigDecimal valorPlano;

    @Enumerated(EnumType.STRING)
    private TipoPlano tipoPlano;

    private Integer duracaoEmDias;

    @Enumerated(EnumType.STRING)
    private StatusPlano statusPlano;

    @OneToMany(mappedBy = "plano")
    private List<Assinatura> assinatura;

    private boolean possuiPersonal;
}
