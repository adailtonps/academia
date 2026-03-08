package com.academia.controller;

import com.academia.dto.UsuarioCadastroDto;
import com.academia.dto.UsuarioResponseDto;
import com.academia.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthAdmiController {
    private final UsuarioService usuarioService;

    @PostMapping("/admin")
    public ResponseEntity<UsuarioResponseDto> cadastrarAdmin(@RequestBody UsuarioCadastroDto usuarioCadastroDto){
        UsuarioResponseDto user =  usuarioService.cadastrarAdmi(usuarioCadastroDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }


}
