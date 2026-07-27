package com.academia.domain;

import com.academia.enums.StatusUsuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.academia.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Usuario implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername(){
        return email;
    }

    @Override
    public String getPassword(){
        return senha;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @NotBlank(message = "Nome obrigatório!")
    private String nome;

    @Column(unique = true)
    @NotBlank(message = "Email é obrigatório!")
    @Email(message = "Email inválido")
    private String email;

    @Column(unique = true, nullable = false)
    private String matricula;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status_user")
    private StatusUsuario status_user;

    @OneToMany(mappedBy = "usuario")
    private List<Checkin> checkin;

    public Usuario(String nome, String email, String matricula, String senha, Role role, StatusUsuario status_user, List<Checkin> checkin) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.senha = senha;
        this.role = role;
        this.status_user = status_user;
        this.checkin = checkin;
    }
}
