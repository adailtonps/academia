package com.academia.domain;

import com.academia.enums.StatusAssinatura;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Assinatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assinatura")
    private int id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_plano")
    private Plano plano;

    private LocalDateTime dataInicio;
    private LocalDateTime dataVencimento;
    private LocalDateTime dataCancelamento;

    @Enumerated(EnumType.STRING)
    private StatusAssinatura statusAssinatura;

}
