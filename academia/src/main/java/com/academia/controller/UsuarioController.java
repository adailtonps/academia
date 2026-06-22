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

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseAdmin>> listarUsuario() {
        List<UsuarioResponseAdmin> listaDeUsersCadastrados = usuarioService.listarUserCadastrados();
        return ResponseEntity.ok(listaDeUsersCadastrados);
    }

    @PutMapping("/me/atualizar")
    public ResponseEntity<AtualizacaoUsuarioResponse> atualizarUsuario(
            @RequestBody UsuarioAtualizarDto  usuarioAtualizarDto,
            @AuthenticationPrincipal Usuario usuarioLogado){

        AtualizacaoUsuarioResponse response = usuarioService.atualizarUsuario(usuarioLogado.getId(), usuarioAtualizarDto);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/me/checkin")
    public ResponseEntity<MensageReturnDto> fazerCheckin(@AuthenticationPrincipal Usuario usuarioLogado){
        usuarioService.checkinUsuario(usuarioLogado);
        return ResponseEntity.ok(new MensageReturnDto("Checkin feito com sucesso! Não se esqueça de fazer o checkout."));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("me/checkout")
    public ResponseEntity<MensageReturnDto> fazerCheckout(@AuthenticationPrincipal Usuario usuarioLogado){
        usuarioService.checkoutUsuario(usuarioLogado);
        return ResponseEntity.ok(new MensageReturnDto("Checkout feito com sucesso!"));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponseDto> desativarUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenhaDto, @AuthenticationPrincipal Usuario usuarioLogado){
        UsuarioResponseDto userDesativar = usuarioService.desativarUsuario(id, confirmarSenhaDto.getSenha(), usuarioLogado);
        return ResponseEntity.ok(userDesativar);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<UsuarioResponseDto> ativarUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenhaDto,  @AuthenticationPrincipal Usuario usuarioLogado){
        UsuarioResponseDto userAtivar = usuarioService.ativarUsuario(id, confirmarSenhaDto.getSenha(), usuarioLogado);
        return ResponseEntity.ok(userAtivar);
    }

    @GetMapping("/me/listarCheckins")
    public ResponseEntity<List<CheckinResponseDto>> listarCheckins(@AuthenticationPrincipal Usuario usuarioLogado){
        List<CheckinResponseDto> checkinsPresentes = usuarioService.historicoCheckins(usuarioLogado);
        return ResponseEntity.ok(checkinsPresentes);
    }

    @GetMapping("/me/listarCheckinsTodos")
    public ResponseEntity<List<CheckinResponseDto>> listarCheckinsTodos(@AuthenticationPrincipal Usuario usuarioLogado){
        List<CheckinResponseDto> checkinsPresentes = usuarioService.historicoCheckinTodos(usuarioLogado);
        return ResponseEntity.ok(checkinsPresentes);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensageReturnDto> deleteUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenha, @AuthenticationPrincipal Usuario usuarioLogado){
        usuarioService.apagarUsuario(id,  confirmarSenha.getSenha(),  usuarioLogado);
        return ResponseEntity.ok(new MensageReturnDto("Usuário apagado com sucesso!"));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me/minhaConta")
    public UsuarioResponseDto minhaConta(@AuthenticationPrincipal Usuario usuarioLogado){
        return usuarioService.minhaConta(usuarioLogado);
    }
}
