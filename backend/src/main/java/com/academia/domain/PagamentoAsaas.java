package com.academia.domain;

import com.academia.enums.StatusPagamento;
import com.academia.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoAsaas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Assinatura assinatura;

    private String asaasPaymentId;

    private BigDecimal valor;

    private LocalDate dataVencimento;

    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

}
