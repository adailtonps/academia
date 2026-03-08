package com.academia.service;

import com.academia.domain.Checkin;
import com.academia.domain.Usuario;
import com.academia.dto.*;
import com.academia.enums.Role;
import com.academia.enums.StatusUsuario;
import com.academia.exception.*;
import com.academia.repository.CheckinRepository;
import com.academia.repository.UsuarioRepository;
import com.academia.response.AtualizacaoUsuarioResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CheckinRepository checkinRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,  PasswordEncoder encoder,  CheckinRepository checkinRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = encoder;
        this.checkinRepository = checkinRepository;
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
        usuario.setRole(Role.ROLE_USER);
        usuario.setStatusUser(StatusUsuario.ATIVADO);

        Usuario usuarioCadastrado =  usuarioRepository.save(usuario);

        return new UsuarioResponseDto(
                usuarioCadastrado.getId(),
                usuarioCadastrado.getMatricula(),
                usuarioCadastrado.getNome(),
                usuarioCadastrado.getEmail(),
                usuarioCadastrado.getStatusUser()
        );
    }

    @Transactional
    public UsuarioResponseDto cadastrarAdmi(UsuarioCadastroDto usuarioCadastroDto){
        if(usuarioRepository.existsByEmail(usuarioCadastroDto.getEmail())) {
            throw new EmailJaCadastradoException("Email ja cadastrado!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCadastroDto.getNome());
        usuario.setEmail(usuarioCadastroDto.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioCadastroDto.getSenha()));
        usuario.setMatricula(gerarMatriculaUnica());
        usuario.setRole(Role.ROLE_ADMIN);
        usuario.setStatusUser(StatusUsuario.ATIVADO);

        Usuario usuarioCadastrado =  usuarioRepository.save(usuario);

        return new UsuarioResponseDto(
                usuarioCadastrado.getId(),
                usuarioCadastrado.getMatricula(),
                usuarioCadastrado.getNome(),
                usuarioCadastrado.getEmail(),
                usuarioCadastrado.getStatusUser()
        );
    }

    @Transactional
    public List<UsuarioResponseAdmin> listarUserCadastrados(){
        List<Usuario> usersCadastrados = usuarioRepository.findAll();
        if(usersCadastrados.isEmpty()){
            throw new UserNaoEncontradoException("Nenhum usuário cadastrado!");
        }

        return usersCadastrados.stream()
                .map(userCad -> new UsuarioResponseAdmin(
                        userCad.getId(),
                        userCad.getNome(),
                        userCad.getEmail(),
                        userCad.getMatricula(),
                        userCad.getStatusUser(),
                        userCad.getRole()
                        )
                ).toList();
    }

    @Transactional
    public AtualizacaoUsuarioResponse atualizarUsuario(Long id_usuario, UsuarioAtualizarDto usuarioAtualizarDto){
        Usuario users = usuarioRepository.findById(id_usuario).orElseThrow(() ->
                new UserNaoEncontradoException("Usuário não encontrado!"));
        
        boolean emailAlterado = false;
        if(usuarioAtualizarDto.getNome() != null){
            users.setNome(usuarioAtualizarDto.getNome());
        }

        if(usuarioAtualizarDto.getEmail() != null &&!usuarioAtualizarDto.getEmail().equals(users.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioAtualizarDto.getEmail())) {
                throw new EmailJaCadastradoException("Email já cadastrado!");
            }
            users.setEmail(usuarioAtualizarDto.getEmail());
            emailAlterado = true;
        }

        Usuario usuarioAtualizado =  usuarioRepository.save(users);

        UsuarioResponseDto dto = new UsuarioResponseDto(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getMatricula(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getStatusUser()
        );
        return new AtualizacaoUsuarioResponse(dto, emailAlterado);
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

        if(usuarioExist.getStatusUser().equals(StatusUsuario.DESATIVADO)){
            throw new UserJaDesativadoException("Usuário já desativado!");
        }

        if(usuarioLogado.getRole().equals(Role.ROLE_ADMIN) && usuarioLogado.getId().equals(usuarioExist.getId())){
            throw new AdminiNaoPodeDesativarException("Administradores não podem desativar a própria conta!");
        }

        if(senha == null || senha.isBlank()){
            throw new SenhaObrigatoriaException("Senha obrigatória!");
        }

        if(!passwordEncoder.matches(senha,  usuarioLogado.getSenha())){
            throw new SenhaIncorretaException("Senha incorreta!");
        }

        usuarioExist.setStatusUser(StatusUsuario.DESATIVADO);

        return new UsuarioResponseDto(
                usuarioExist.getId(),
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

        if(!usuarioLogado.getRole().equals(Role.ROLE_ADMIN)){
            throw new AdminApagarContaException("Somente administradores podem apagar contas!");
        }

        if(usuarioLogado.getId().equals(userExist.getId())){
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

    @Transactional
    public CheckinResponseDto checkinUsuario(Usuario usuarioLogado){
        Optional<Checkin> checkinAtivo = checkinRepository.findByUsuarioAndCheckoutIsNull(usuarioLogado);
        if(checkinAtivo.isPresent()){
            throw new IllegalArgumentException("Você já tem um check-in ativo!");
        }

        Optional<Checkin> ultimoCheckin = checkinRepository.findTopByUsuarioOrderByCheckinDesc(usuarioLogado);
        if(ultimoCheckin.isPresent() && ultimoCheckin.get().getCheckin().toLocalDate().equals(LocalDate.now())){
            throw new IllegalArgumentException("Você só pode fazer um checkin por dia!");
        }

        Checkin criarCheckin = new Checkin();

        criarCheckin.setUsuario(usuarioLogado);
        criarCheckin.setCheckin(LocalDateTime.now());

        checkinRepository.save(criarCheckin);

        return new CheckinResponseDto(
                usuarioLogado.getNome(),
                usuarioLogado.getId(),
                criarCheckin.getCheckin(),
                criarCheckin.getCheckout()
        );

    }
}
