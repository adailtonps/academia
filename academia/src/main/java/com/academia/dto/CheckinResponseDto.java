package com.academia.dto;

import com.academia.domain.Usuario;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CheckinResponseDto {
    private String nome;
    private Long id_checkin;
    private LocalDateTime checkin;
    private LocalDateTime checkout;


    public CheckinResponseDto(String nome, Long id_checkin, LocalDateTime checkin, LocalDateTime checkout) {
        this.nome = nome;
        this.id_checkin = id_checkin;
        this.checkin = checkin;
        this.checkout = checkout;
    }
}
