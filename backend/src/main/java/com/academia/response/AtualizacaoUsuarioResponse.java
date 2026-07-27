package com.academia.response;

import com.academia.dto.UsuarioResponseDto;

public class AtualizacaoUsuarioResponse {
    private UsuarioResponseDto usuario;
    private boolean emailAlterado;

    public AtualizacaoUsuarioResponse(UsuarioResponseDto usuario, boolean emailAlterado) {
        this.usuario = usuario;
        this.emailAlterado = emailAlterado;
    }

    public UsuarioResponseDto getUsuario() {
        return usuario;
    }

    public boolean isEmailAlterado() {
        return emailAlterado;
    }
}
