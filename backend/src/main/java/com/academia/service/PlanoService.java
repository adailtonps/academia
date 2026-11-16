package com.academia.service;

import com.academia.domain.Assinatura;
import com.academia.domain.Plano;
import com.academia.domain.Usuario;
import com.academia.dto.CadastroPlanoDto;
import com.academia.dto.CadastroPlanoResponseDto;
import com.academia.dto.MensageReturnDto;
import com.academia.enums.StatusAssinatura;
import com.academia.enums.TipoPlano;
import com.academia.exception.RegraNegocioException;
import com.academia.exception.UserNaoEncontradoException;
import com.academia.repository.AssinaturaRepository;
import com.academia.repository.PlanoRepository;
import com.academia.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanoService {
    private final PlanoRepository planoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AssinaturaRepository assinaturaRepository;

    public CadastroPlanoResponseDto cadastrarPlano(CadastroPlanoDto cadastroPlanoDto, Usuario usuario){
        Usuario admin = usuarioRepository.findById(usuario.getId()).
                orElseThrow(() -> new UserNaoEncontradoException("Administrador não encontrado!"));

        if (planoRepository.existsByNomePlanoAndValorPlanoAndDuracaoEmDiasAndPossuiPersonal(
                cadastroPlanoDto.getNomePlano(),
                cadastroPlanoDto.getValorPlano(),
                cadastroPlanoDto.getDuracaoEmDias(),
                cadastroPlanoDto.isPossuiPersonal())){
            throw new RegraNegocioException("Um plano igual a este já foi cadastrado!");

        } else {
            Plano plano = new Plano();
            plano.setAdmin(admin);
            plano.setNomePlano(cadastroPlanoDto.getNomePlano().toUpperCase());
            plano.setDescricao(cadastroPlanoDto.getDescricao().toUpperCase());
            plano.setValorPlano(cadastroPlanoDto.getValorPlano());
            plano.setTipoPlano(cadastroPlanoDto.getTipoPlano());

            if(cadastroPlanoDto.getTipoPlano().equals(TipoPlano.MENSAL)){
                plano.setDuracaoEmDias(30);
            } else if(cadastroPlanoDto.getTipoPlano().equals(TipoPlano.TRIMESTRAL)){
                plano.setDuracaoEmDias(90);
            } else if(cadastroPlanoDto.getTipoPlano().equals(TipoPlano.ANUAL)) {
                plano.setDuracaoEmDias(365);
            } else {
                throw new RegraNegocioException("Tipo de plano indisponível, selecione MENSAL, TRIMESTRAL OU ANUAL!");
            }
            plano.setStatusPlano(cadastroPlanoDto.getStatusPlano());
            plano.setPossuiPersonal(cadastroPlanoDto.isPossuiPersonal());
            Plano planoCadastrado = planoRepository.save(plano);

        return new CadastroPlanoResponseDto(
                planoCadastrado.getNomePlano(),
                planoCadastrado.getDescricao(),
                planoCadastrado.getValorPlano(),
                planoCadastrado.getTipoPlano(),
                planoCadastrado.getStatusPlano(),
                planoCadastrado.getDuracaoEmDias(),
                planoCadastrado.isPossuiPersonal()
        );
    }
    }

    public void escolherPlano(Long idPlano, Usuario usuario){
        Plano existePlano = planoRepository.findById(idPlano)
                .orElseThrow(() -> new RegraNegocioException("Plano não encontrado!"));

        if(assinaturaRepository.existsByUsuario_IdAndStatusAssinatura(
                usuario.getId(), StatusAssinatura.PENDENTE))
        {
            throw new RegraNegocioException("Você já tem um pagamento de assinatura pendente!");
        }

        if(assinaturaRepository.existsByUsuario_IdAndStatusAssinatura
                (usuario.getId(), StatusAssinatura.ATIVA))
        {
            throw new RegraNegocioException("Você já possui uma assinatura ativa.");
        } else {
            Assinatura assinatura = new Assinatura();
            assinatura.setUsuario(usuario);
            assinatura.setPlano(existePlano);
            assinatura.setDataInicio(null);
            assinatura.setDataVencimento(null);
            assinatura.setValorAssinatura(existePlano.getValorPlano());
            assinatura.setStatusAssinatura(StatusAssinatura.PENDENTE);

            LocalDateTime dataHoraAgora = LocalDateTime.now();

            assinatura.setDataLimitePagamento(dataHoraAgora.plusMinutes(30));
            assinaturaRepository.save(assinatura);
        }
    }

    @Transactional
    @Scheduled(fixedRate = 60000)
    public void AssinaturaSchedelur(){
        LocalDateTime dataHoraAgora = LocalDateTime.now();
        List<Assinatura> assinaturasPendentes = assinaturaRepository.findByStatusAssinaturaAndDataLimitePagamentoBefore(
                StatusAssinatura.PENDENTE,
                dataHoraAgora
        );

        for(Assinatura assinatura : assinaturasPendentes){
            assinatura.setStatusAssinatura(StatusAssinatura.EXPIRADA);
            assinaturaRepository.save(assinatura);
        }
    }
}
