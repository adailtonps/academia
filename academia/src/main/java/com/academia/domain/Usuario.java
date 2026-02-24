package com.academia.domain;

import com.academia.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.academia.enums.Role;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuario;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String matricula;

    private String senha;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private StatusUsuario statusUser;

    @OneToMany(mappedBy = "usuario")
    private List<Checkin> checkin;

    public Usuario(String nome, String email, String matricula, String senha, Role role, StatusUsuario statusUser, List<Checkin> checkin) {
        this.nome = nome;
        this.email = email;
        this.matricula = matricula;
        this.senha = senha;
        this.role = role;
        this.statusUser = statusUser;
        this.checkin = checkin;
    }
}
