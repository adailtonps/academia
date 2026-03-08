package com.academia.controller;

import com.academia.domain.Checkin;
import com.academia.domain.Usuario;
import com.academia.dto.*;
import com.academia.repository.UsuarioRepository;
import com.academia.response.AtualizacaoUsuarioResponse;
import com.academia.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    public UsuarioRepository  usuarioRepository;
    public UsuarioService  usuarioService;

    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseAdmin>> listarUsuario() {
        List<UsuarioResponseAdmin> listaDeUsersCadastrados = usuarioService.listarUserCadastrados();
        return ResponseEntity.ok(listaDeUsersCadastrados);
    }

    @PutMapping("/me")
    public ResponseEntity<?> atualizarUsuario(
            @RequestBody UsuarioAtualizarDto  usuarioAtualizarDto,
            @AuthenticationPrincipal Usuario usuarioLogado){

        AtualizacaoUsuarioResponse response = usuarioService.atualizarUsuario(usuarioLogado.getId(), usuarioAtualizarDto);

        if(response.isEmailAlterado()){
            return ResponseEntity.ok(
                    "Email atualizado. Faça login novamente!");
        }
        return ResponseEntity.ok(response.getUsuario());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{me}/checkin")
    public ResponseEntity<String> fazerCheckin(@AuthenticationPrincipal Usuario usuarioLogado){
        usuarioService.checkinUsuario(usuarioLogado);
        return ResponseEntity.ok("Checkin feito com sucesso! Não se esqueça de fazer o checkout.");
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponseDto> desativarUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenhaDto, @AuthenticationPrincipal Usuario usuarioLogado){
        UsuarioResponseDto userDesativar = usuarioService.desativarUsuario(id, confirmarSenhaDto.getSenha(), usuarioLogado);
        return ResponseEntity.ok(userDesativar);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenha, @AuthenticationPrincipal Usuario usuarioLogado){
        usuarioService.apagarUsuario(id,  confirmarSenha.getSenha(),  usuarioLogado);
        return ResponseEntity.ok("Usuário apagado com sucesso!");
    }
}
