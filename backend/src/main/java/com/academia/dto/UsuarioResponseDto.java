package com.academia.dto;

import com.academia.enums.StatusUsuario;
import lombok.*;

@Getter
@Setter
public class UsuarioResponseDto {
    private Long id_usuario;
    private String nome;
    private String email;
    private StatusUsuario  statusUsuario;
    private String matricula;

    public UsuarioResponseDto(Long id_usuario, String matricula, String nome, String email, StatusUsuario statusUsuario) {
        this.id_usuario = id_usuario;
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.statusUsuario = statusUsuario;
    }
}
