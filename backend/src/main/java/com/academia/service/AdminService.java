package com.academia.service;

import com.academia.domain.Checkin;
import com.academia.domain.Usuario;
import com.academia.dto.*;
import com.academia.enums.Role;
import com.academia.enums.StatusUsuario;
import com.academia.exception.RegraNegocioException;
import com.academia.exception.UserNaoEncontradoException;
import com.academia.repository.CheckinRepository;
import com.academia.repository.UsuarioRepository;
import com.academia.response.AtualizacaoUsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CheckinRepository checkinRepository;

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

    private void senhaSeguranca(String senha){
        boolean contemLetraMaiuscula = false;
        boolean contemNumero = false;
        boolean contemCaractereEspecial = false;

        if(senha.length() < 8){
            throw new RegraNegocioException("A senha precisa conter no mínimo 8 caracteres!");
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
            throw new RegraNegocioException("A senha precisa conter no mínimo 1 letra maiúscula, " +
                    "1 número e 1 caractere especial (!@#$%&*?)");
        }
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
                                userCad.getStatus_user(),
                                userCad.getRole()
                        )
                ).toList();
    }

    @Transactional
    public UsuarioResponseDto cadastrarAdmi(UsuarioCadastroDto usuarioCadastroDto){
        if(usuarioRepository.existsByEmail(usuarioCadastroDto.getEmail())) {
            throw new RegraNegocioException("Email já cadastrado!");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioCadastroDto.getNome().toUpperCase());
        usuario.setEmail(usuarioCadastroDto.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioCadastroDto.getSenha()));
        usuario.setMatricula(gerarMatriculaUnica());
        usuario.setRole(Role.ROLE_ADMIN);
        usuario.setStatus_user(StatusUsuario.ATIVADO);

        senhaSeguranca(usuarioCadastroDto.getSenha());
        Usuario usuarioCadastrado =  usuarioRepository.save(usuario);

        return new UsuarioResponseDto(
                usuarioCadastrado.getId(),
                usuarioCadastrado.getMatricula(),
                usuarioCadastrado.getNome(),
                usuarioCadastrado.getEmail(),
                usuarioCadastrado.getStatus_user()
        );
    }

    public List<CheckinResponseDto> historicoCheckinTodos(Usuario usuarioLogado){
        List<Checkin> checkins = checkinRepository.findAll();
        if(checkins.isEmpty()){
            throw new RegraNegocioException("Nenhum checkin cadastrado!");
        }
        return checkins.stream()
                .map(checkinsPresent -> new CheckinResponseDto(
                        checkinsPresent.getUsuario().getNome(),
                        checkinsPresent.getUsuario().getEmail(),
                        checkinsPresent.getUsuario().getId(),
                        checkinsPresent.getId_checkin(),
                        checkinsPresent.getCheckin(),
                        checkinsPresent.getCheckout(),
                        checkinsPresent.getTempoDePermanencia()
                )).toList();
    }

    @Transactional
    public void apagarUsuario(Long id_usuario, String senha, Usuario usuarioLogado){
        Usuario userExist = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new UserNaoEncontradoException("Usuário já apagado ou não existe!"));

        if(userExist.getStatus_user().equals(StatusUsuario.ATIVADO)){
            throw new RegraNegocioException("Desative a conta antes de apagar!");
        }

        if(!usuarioLogado.getRole().equals(Role.ROLE_ADMIN)){
            throw new RegraNegocioException("Somente administradores podem apagar contas!");
        }

        if(usuarioLogado.getId().equals(userExist.getId())){
            throw new RegraNegocioException("Administradores não podem apagar a própria conta!");
        }

        if(senha == null || senha.isBlank()){
            throw new RegraNegocioException("Senha obrigatória!");
        }

        if(!passwordEncoder.matches(senha, usuarioLogado.getSenha())){
            throw new RegraNegocioException("Senha incorreta!");
        }
        checkinRepository.deleteByUsuario_id(userExist.getId());
        usuarioRepository.delete(userExist);
    }

    @Transactional
    public UsuarioResponseDto ativarUsuario(Long id_usuario, String senha, Usuario usuarioLogado){
        Usuario usuarioExist = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new UserNaoEncontradoException("Usuário não encontrado!"));

        if(senha == null || senha.isBlank()){
            throw new RegraNegocioException("A senha precisa ser preenchida!");
        }

        if(!passwordEncoder.matches(senha, usuarioLogado.getSenha())){
            throw new RegraNegocioException("Senha incorreta!");
        }

        if(!usuarioLogado.getId().equals(id_usuario) && !usuarioLogado.getRole().equals(Role.ROLE_ADMIN)){
            throw new RegraNegocioException("Você só pode ativar sua própria conta!");
        }

        if(usuarioExist.getStatus_user().equals(StatusUsuario.ATIVADO)) {
            throw new RegraNegocioException("Usuário já ativado!");
        }

        usuarioExist.setStatus_user(StatusUsuario.ATIVADO);
        usuarioRepository.save(usuarioExist);
        return new UsuarioResponseDto(
                usuarioExist.getId(),
                usuarioExist.getMatricula(),
                usuarioExist.getNome(),
                usuarioExist.getEmail(),
                usuarioExist.getStatus_user()
        );
    }


    @Transactional
    public UsuarioResponseDto desativarUsuario(Long id_usuario, String senha, Usuario usuarioLogado){
        Usuario usuarioExist = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new UserNaoEncontradoException("Usuário não encontrado!"));

        if(senha == null || senha.isBlank()){
            throw new RegraNegocioException("Senha obrigatória!");
        }

        if(!passwordEncoder.matches(senha,  usuarioLogado.getSenha())){
            throw new RegraNegocioException("Senha incorreta!");
        }

        if(!usuarioLogado.getId().equals(id_usuario) && !usuarioLogado.getRole().equals(Role.ROLE_ADMIN)){
            throw new RegraNegocioException("Você só pode desativar sua própria conta!");
        }

        if(usuarioExist.getStatus_user().equals(StatusUsuario.DESATIVADO)){
            throw new RegraNegocioException("Usuário já desativado!");
        }

        if(usuarioLogado.getRole().equals(Role.ROLE_ADMIN) && usuarioLogado.getId().equals(usuarioExist.getId())){
            throw new RegraNegocioException("Administradores não podem desativar a própria conta!");
        }

        usuarioExist.setStatus_user(StatusUsuario.DESATIVADO);

        return new UsuarioResponseDto(
                usuarioExist.getId(),
                usuarioExist.getMatricula(),
                usuarioExist.getNome(),
                usuarioExist.getEmail(),
                usuarioExist.getStatus_user()
        );
    }

    public AtualizacaoUsuarioResponse atualizarUsuario(Long id_usuario, UsuarioAtualizarDto usuarioAtualizarDto){
        Usuario users = usuarioRepository.findById(id_usuario).orElseThrow(() ->
                new UserNaoEncontradoException("Usuário não encontrado!"));

        boolean emailAlterado = false;
        if(usuarioAtualizarDto.getNome() != null && !usuarioAtualizarDto.getNome().isBlank()){
            users.setNome(usuarioAtualizarDto.getNome().toUpperCase());
        }

        if(usuarioAtualizarDto.getEmailAlterado() != null && !usuarioAtualizarDto.getEmailAlterado().isBlank() && !usuarioAtualizarDto.getEmailAlterado().equals(users.getEmail())) {
            if(!usuarioAtualizarDto.getEmailAlterado().contains("@")){
                throw new RegraNegocioException("Digite um email inválido!");
            }
            if (usuarioRepository.existsByEmail(usuarioAtualizarDto.getEmailAlterado())) {
                throw new RegraNegocioException("Email já cadastrado!");
            }
            users.setEmail(usuarioAtualizarDto.getEmailAlterado().trim().toLowerCase());
            emailAlterado = true;
        }

        Usuario usuarioAtualizado =  usuarioRepository.save(users);

        UsuarioResponseDto dto = new UsuarioResponseDto(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getMatricula(),
                usuarioAtualizado.getNome(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getStatus_user()
        );
        return new AtualizacaoUsuarioResponse(dto, emailAlterado);
    }
}
