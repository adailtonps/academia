package com.academia.dto;

import com.academia.enums.StatusUsuario;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCadastroDto {
    @NotBlank(message = "Nome obrigatório!")
    private String nome;

    @NotBlank(message = "Email obrigatório!")
    @Email(message = "Email inválido!")
    private String email;

    @NotBlank(message = "Senha obrigatória!")
    private String senha;

    private StatusUsuario status;

    public UsuarioCadastroDto(String nome, String email, String senha, StatusUsuario status) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.status = status;
    }
}
