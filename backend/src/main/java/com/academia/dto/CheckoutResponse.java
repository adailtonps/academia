package com.academia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckoutResponse {
    private String nome;
    private Long id_user;
    private Long id_checkout;
    private LocalDateTime  checkout;
    private LocalDateTime  checkin;

    public CheckoutResponse(String nome, Long id_checkout, Long id_user, LocalDateTime checkin, LocalDateTime checkout) {
        this.nome = nome;
        this.id_checkout = id_checkout;
        this.id_user = id_user;
        this.checkin = checkin;
        this.checkout = checkout;
    }
}
