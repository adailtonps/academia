package com.academia.jwt;

import com.academia.domain.Usuario;
import com.academia.repository.UsuarioRepository;
import com.academia.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class FiltroJwt extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UsuarioRepository usuarioRepository;

    public FiltroJwt(UsuarioRepository usuarioRepository, JWTService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                        throws ServletException, IOException {
        SecurityContextHolder.clearContext();

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.replace("Bearer ", "");

            var claims = jwtService.getClaims(token);

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            if (email != null && role != null) {

                Usuario usuario = usuarioRepository
                        .findByEmail(email)
                        .orElse(null);

                if(usuario != null) {
                    var authorities = List.of(new SimpleGrantedAuthority(role));

                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario,
                            null,
                            authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                }
        }
        filterChain.doFilter(request, response);
    }
}
