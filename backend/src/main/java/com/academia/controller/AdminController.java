package com.academia.controller;

import com.academia.domain.Usuario;
import com.academia.dto.*;
import com.academia.repository.UsuarioRepository;
import com.academia.response.AtualizacaoUsuarioResponse;
import com.academia.service.AdminService;
import com.academia.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponseAdmin>> listarUsuario() {
        List<UsuarioResponseAdmin> listaDeUsersCadastrados = adminService.listarUserCadastrados();

        return ResponseEntity.ok(listaDeUsersCadastrados);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensageReturnDto> deleteUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenha, @AuthenticationPrincipal Usuario usuarioLogado){
        adminService.apagarUsuario(id,  confirmarSenha.getSenha(),  usuarioLogado);
        return ResponseEntity.ok(new MensageReturnDto("Usuário apagado com sucesso!"));
    }

    @GetMapping("/me/listarCheckinsTodos")
    public ResponseEntity<List<CheckinResponseDto>> listarCheckinsTodos(@AuthenticationPrincipal Usuario usuarioLogado){
        List<CheckinResponseDto> checkinsPresentes = adminService.historicoCheckinTodos(usuarioLogado);
        return ResponseEntity.ok(checkinsPresentes);
    }

    @PutMapping("/me/atualizar")
    public ResponseEntity<AtualizacaoUsuarioResponse> atualizarUsuario(
            @RequestBody UsuarioAtualizarDto usuarioAtualizarDto,
            @AuthenticationPrincipal Usuario usuarioLogado){

        AtualizacaoUsuarioResponse response = adminService.atualizarUsuario(usuarioLogado.getId(), usuarioAtualizarDto);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<UsuarioResponseDto> desativarUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenhaDto, @AuthenticationPrincipal Usuario usuarioLogado){
        UsuarioResponseDto userDesativar = adminService.desativarUsuario(id, confirmarSenhaDto.getSenha(), usuarioLogado);
        return ResponseEntity.ok(userDesativar);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<UsuarioResponseDto> ativarUsuario(@PathVariable Long id, @RequestBody ConfirmarSenhaDto confirmarSenhaDto,  @AuthenticationPrincipal Usuario usuarioLogado){
        UsuarioResponseDto userAtivar = adminService.ativarUsuario(id, confirmarSenhaDto.getSenha(), usuarioLogado);
        return ResponseEntity.ok(userAtivar);
    }


}
