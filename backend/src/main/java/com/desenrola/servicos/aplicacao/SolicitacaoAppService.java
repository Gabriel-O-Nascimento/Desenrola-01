package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.AtualizarStatusDTO;
import com.desenrola.controladores.dto.CriarSolicitacaoDTO;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.ServicoRepository;
import com.desenrola.repositorios.SolicitacaoRepository;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.repositorios.entidades.Servico;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.repositorios.entidades.StatusSolicitacao;
import com.desenrola.servicos.SolicitacaoService;
import com.desenrola.servicos.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitacaoAppService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ServicoRepository servicoRepository;
    private final SolicitacaoService solicitacaoService;
    private final StatusService statusService;

    @Transactional
    public Solicitacao criar(CriarSolicitacaoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Servico servico = servicoRepository.findById(dto.getIdServico())
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado."));

        LocalDateTime agora = LocalDateTime.now();

        Solicitacao solicitacao = Solicitacao.builder()
                .cliente(cliente)
                .servico(servico)
                .status(StatusSolicitacao.AGUARDANDO_ORCAMENTO)
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .enderecoAtendimento(dto.getEnderecoAtendimento())
                .cidadeAtendimento(dto.getCidadeAtendimento())
                .estadoAtendimento(dto.getEstadoAtendimento())
                .valorEstimado(dto.getValorEstimado())
                .dataPreferencial(dto.getDataPreferencial())
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        Solicitacao salva = solicitacaoRepository.save(solicitacao);

        solicitacaoService.criarSolicitacao(
                salva.getId(),
                cliente.getIdUsuario(),
                servico.getCategoria() != null ? servico.getCategoria().getNome() : "Sem categoria",
                salva.getDescricao(),
                salva.getEnderecoAtendimento()
        );

        return salva;
    }

    @Transactional
    public Solicitacao atualizarStatus(Long idSolicitacao, AtualizarStatusDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(idSolicitacao)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        StatusSolicitacao statusAnterior = solicitacao.getStatus();
        solicitacao.setStatus(dto.getNovoStatus());
        solicitacao.setAtualizadoEm(LocalDateTime.now());

        if (dto.getIdProfissional() != null) {
            Profissional profissional = profissionalRepository.findById(dto.getIdProfissional())
                    .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado."));
            solicitacao.setProfissional(profissional);
        }

        if (dto.getNovoStatus() == StatusSolicitacao.CANCELADA) {
            solicitacao.setMotivoCancelamento(dto.getMotivoCancelamento());
        }

        if (dto.getNovoStatus() == StatusSolicitacao.CONCLUIDA) {
            solicitacao.setDataConclusao(LocalDateTime.now());
        }

        Solicitacao atualizada = solicitacaoRepository.save(solicitacao);

        statusService.atualizarStatus(
                atualizada.getId(),
                atualizada.getCliente().getIdUsuario(),
                atualizada.getProfissional() != null ? atualizada.getProfissional().getIdUsuario() : null,
                statusAnterior.name(),
                atualizada.getStatus().name()
        );

        return atualizada;
    }

    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));
    }

    public List<Solicitacao> listarPorCliente(Long idCliente) {
        return solicitacaoRepository.findByClienteIdUsuarioOrderByCriadoEmDesc(idCliente);
    }

    public List<Solicitacao> listarPorProfissional(Long idProfissional) {
        return solicitacaoRepository.findByProfissionalIdUsuarioOrderByCriadoEmDesc(idProfissional);
    }
}
