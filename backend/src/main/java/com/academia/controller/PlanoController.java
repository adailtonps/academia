package com.academia.controller;


import com.academia.domain.Usuario;
import com.academia.dto.CadastroPlanoDto;
import com.academia.dto.CadastroPlanoResponseDto;
import com.academia.repository.PlanoRepository;
import com.academia.service.PlanoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("plano")
public class PlanoController {
    private final PlanoRepository planoRepository;
    private final PlanoService planoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CadastroPlanoResponseDto> cadastrarPlano(@RequestBody CadastroPlanoDto cadastroPlanoDto,
                                                                   @AuthenticationPrincipal Usuario adminLogado){
        CadastroPlanoResponseDto planoCadastrado = planoService.cadastrarPlano(cadastroPlanoDto, adminLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(planoCadastrado);
    }
}
