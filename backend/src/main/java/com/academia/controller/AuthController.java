package com.academia.controller;

import com.academia.dto.LoginDto;
import com.academia.dto.LoginResponseDto;
import com.academia.dto.UsuarioCadastroDto;
import com.academia.dto.UsuarioResponseDto;
import com.academia.service.JWTService;
import com.academia.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponseDto> cadastro(@RequestBody UsuarioCadastroDto usuarioCadastroDto) {
        UsuarioResponseDto userCadastrado = usuarioService.cadastrarUsuario(usuarioCadastroDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCadastrado);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        System.out.println("1 - Login recebido: " + loginDto.getEmail());
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getSenha()
                )
        );
        System.out.println("2 - Autenticação realizada com sucesso");
        String jwt = jwtService.gerarToken(authentication);
        System.out.println("3 - JWT gerado com sucesso");
        return ResponseEntity.ok(new LoginResponseDto("login realizado com sucesso!",jwt));
    }
}
