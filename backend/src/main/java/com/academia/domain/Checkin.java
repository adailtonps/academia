package com.academia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_checkin;

    private LocalDateTime checkin;

    private LocalDateTime checkout;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
