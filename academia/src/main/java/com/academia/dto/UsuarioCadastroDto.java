package com.academia.dto;

import com.academia.enums.StatusUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastroDto {
    private String nome;
    private String email;
    private String senha;
    private StatusUsuario status;

    public UsuarioCadastroDto(String nome, String email, String senha, StatusUsuario status) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.status = status;
    }
}
