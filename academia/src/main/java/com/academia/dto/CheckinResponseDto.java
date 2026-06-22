package com.academia.dto;

import com.academia.domain.Usuario;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckinResponseDto {
    private String nome;
    private String email;
    private Long id_user;
    private Long id_checkin;
    private LocalDateTime checkin;
    private LocalDateTime checkout;


    public CheckinResponseDto(String nome, String email, Long id_user, Long id_checkin, LocalDateTime checkin, LocalDateTime checkout) {
        this.nome = nome;
        this.email = email;
        this.id_user = id_user;
        this.id_checkin = id_checkin;
        this.checkin = checkin;
        this.checkout = checkout;
    }
}
