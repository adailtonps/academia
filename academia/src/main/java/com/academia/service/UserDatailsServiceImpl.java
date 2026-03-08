package com.academia.service;

import com.academia.domain.Usuario;
import com.academia.exception.UserNaoEncontradoException;
import com.academia.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDatailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository  usuarioRepository;

    public UserDatailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado!"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(usuario.getRole().name())
                .build();
    }
}
