package com.academia.service;

import com.academia.domain.Usuario;
import com.academia.dto.UsuarioAtualizarDto;
import com.academia.dto.UsuarioCadastroDto;
import com.academia.dto.UsuarioResponseDto;
import com.academia.enums.Role;
import com.academia.enums.StatusUsuario;
import com.academia.exception.*;
import com.academia.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,  PasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = encoder;
    }

    private String gerarMatricula(){
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder matricula = new StringBuilder();
        Random random = new Random();

        for(int i = 0; i < 7; i++){
            int index = random.nextInt(caracteres.length());
            matricula.append(caracteres.charAt(index));
        }
        return matricula.toString();
    }


    private String gerarMatriculaUnica(){
        String matricula;

        do {
            matricula = gerarMatricula();
        } while(this.usuarioRepository.existsByMatricula(matricula));

        return matricula;
    }

    @Transactional
    public UsuarioResponseDto cadastrarUsuario(UsuarioCadastroDto usuarioCadastroDto){
        if(usuarioRepository.existsByEmail(usuarioCadastroDto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCadastroDto.getNome());
        usuario.setEmail(usuarioCadastroDto.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioCadastroDto.getSenha()));
        usuario.setMatricula(gerarMatriculaUnica());
        usuario.setStatusUser(StatusUsuario.ATIVADO);

        Usuario usuarioCadastrado =  usuarioRepository.save(usuario);

        return new UsuarioResponseDto(
                usuarioCadastrado.getId_usuario(),
                usuarioCadastrado.getMatricula(),
                usuarioCadastrado.getNome(),
                usuarioCadastrado.getEmail(),
                usuarioCadastrado.getStatusUser()
        );
    }

    @Transactional
    public List<UsuarioResponseDto> listarUserCadastrados(){
        List<Usuario> usersCadastrados = usuarioRepository.findAll();
        if(usersCadastrados.isEmpty()){
            throw new UserNaoEncontradoException("Nenhum usuário cadastrado!");
        }

        return usersCadastrados.stream()
                .map(userCad -> new UsuarioResponseDto(
                        userCad.getId_usuario(),
                        userCad.getNome(),
                        userCad.getEmail(),
                        userCad.getMatricula(),
                        userCad.getStatusUser()
                        )
                ).toList();
    }

    @Transactional
    public UsuarioResponseDto atualizarUsuario(Long id_usuario, UsuarioAtualizarDto usuarioAtualizarDto){
        Usuario users = usuarioRepository.findById(id_usuario).orElseThrow(() ->
                new UserNaoEncontradoException("Usuário não encontrado!"));

        if(!usuarioAtualizarDto.getEmail().equals(users.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioAtualizarDto.getEmail())) {
            throw new EmailJaCadastradoException("Email já cadastrado!");
        }

        if(usuarioAtualizarDto.getEmail() == null ||
                !usuarioAtualizarDto.getEmail().contains("@")){
            throw new EmailInvalidoException("Email inválido!");
        }

        users.setNome(usuarioAtualizarDto.getNome());
        users.setEmail(usuarioAtualizarDto.getEmail());

        Usuario usuarioAtualizado =  usuarioRepository.save(users);

        return new UsuarioResponseDto(
                usuarioAtualizado.getId_usuario(),
                usuarioAtualizado.getMatricula(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getStatusUser()
        );
    }

    private void senhaSeguranca(String senha){
        boolean contemLetraMaiuscula = false;
        boolean contemNumero = false;
        boolean contemCaractereEspecial = false;

        if(senha.length() < 8){
            throw new SenhaInvalidaException("A senha precisa conter no mínimo 8 caracteres!");
        }

        for (int i = 0; i < senha.length(); i++) {
            char c = senha.charAt(i);

            if(Character.isUpperCase(c)){
                contemLetraMaiuscula= true;
            }
            if(Character.isDigit(c)){
                contemNumero = true;
            }
            if(!Character.isLetterOrDigit(c)){
                contemCaractereEspecial = true;
            }

            }
            if(!contemNumero || !contemCaractereEspecial || !contemLetraMaiuscula){
                throw new SenhaInvalidaException("A senha precisa conter no mínimo 1 letra maiúscula, " +
                    "1 número e 1 caractere especial (!@#$%&*?)");
        }
    }


    @Transactional
    public UsuarioResponseDto desativarUsuario(Long id_usuario, String senha, Usuario usuarioLogado){
        Usuario usuarioExist = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new UserNaoEncontradoException("Usuário não encontrado!"));

        if(usuarioExist.getStatusUser() == StatusUsuario.DESATIVADO){
            throw new UserJaDesativadoException("Usuário já desativado!");
        }

        if(usuarioLogado.getRole() == Role.ADMIN && usuarioLogado.getId_usuario().equals(usuarioExist.getId_usuario())){
            throw new AdminiNaoPodeDesativarException("Administradores não podem desativar a própria conta!");
        }

        if(senha == null || senha.isBlank()){
            throw new SenhaObrigatoriaException("Senha obrigatória!");
        }

        if(!passwordEncoder.matches(senha,  usuarioLogado.getSenha())){
            throw new SenhaIncorretaException("Senha incorreta!");
        }

        if(usuarioLogado.getRole() != Role.ADMIN && !usuarioLogado.getId_usuario().equals(usuarioExist.getId_usuario())){
            throw new UserNaoPodeApagarException("Você não tem permissão para desativar esse usuário!");
        }

        usuarioExist.setStatusUser(StatusUsuario.DESATIVADO);

        return new UsuarioResponseDto(
                usuarioExist.getId_usuario(),
                usuarioExist.getMatricula(),
                usuarioExist.getNome(),
                usuarioExist.getEmail(),
                usuarioExist.getStatusUser()
        );
    }


    @Transactional
    public void apagarUsuario(Long id_usuario, String senha, Usuario usuarioLogado){
        Usuario userExist = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new EmailJaCadastradoException("Usuário já apagado ou não existe!"));

        if(usuarioLogado.getRole() != Role.ADMIN){
            throw new AdminApagarContaException("Somente administradores podem apagar contas!");
        }

        if(usuarioLogado.getId_usuario().equals(userExist.getId_usuario())){
            throw new AdminNaoApagarContaException("Administradores não podem apagar a própria conta!");
        }

        if(senha == null || senha.isBlank()){
            throw new SenhaObrigatoriaException("Senha obrigatória!");
        }

        if(!passwordEncoder.matches(senha, usuarioLogado.getSenha())){
            throw new SenhaIncorretaException("Senha incorreta!");
        }

        usuarioRepository.delete(userExist);
    }
}
