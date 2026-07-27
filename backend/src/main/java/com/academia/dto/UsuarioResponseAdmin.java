package com.academia.dto;

import com.academia.enums.Role;
import com.academia.enums.StatusUsuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseAdmin {
    private Long id_usuario;
    private String nome;
    private String email;
    private StatusUsuario statusUsuario;
    private String matricula;
    private Role role;

    public UsuarioResponseAdmin(Long id_usuario, String matricula, String nome, String email, StatusUsuario statusUsuario,  Role role) {
        this.id_usuario = id_usuario;
        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.statusUsuario = statusUsuario;
        this.role = role;
    }
}
