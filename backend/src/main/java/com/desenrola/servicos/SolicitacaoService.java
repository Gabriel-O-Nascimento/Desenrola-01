package com.desenrola.servicos;

import com.desenrola.dto.CriarSolicitacaoRequest;
import com.desenrola.dto.SolicitacaoResponse;
import com.desenrola.modelos.Cliente;
import com.desenrola.modelos.Profissional;
import com.desenrola.modelos.Servico;
import com.desenrola.modelos.Solicitacao;
import com.desenrola.modelos.enums.StatusSolicitacao;
import com.desenrola.repositorios.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ClienteService clienteService;
    private final ProfissionalService profissionalService;
    private final ServicoService servicoService;
    private final NotificacaoService notificacaoService;

    public SolicitacaoService(
            SolicitacaoRepository solicitacaoRepository,
            ClienteService clienteService,
            ProfissionalService profissionalService,
            ServicoService servicoService,
            NotificacaoService notificacaoService
    ) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.clienteService = clienteService;
        this.profissionalService = profissionalService;
        this.servicoService = servicoService;
        this.notificacaoService = notificacaoService;
    }

    public SolicitacaoResponse criar(CriarSolicitacaoRequest request) {
        Cliente cliente = clienteService.buscarEntidade(request.clienteId());
        Servico servico = servicoService.buscarEntidade(request.servicoId());

        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setCliente(cliente);
        solicitacao.setServico(servico);
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setEnderecoAtendimento(request.enderecoAtendimento());
        solicitacao.setObservacoes(request.observacoes());
        solicitacao.setValorTotal(BigDecimal.valueOf(servico.getPrecoBase()));
        solicitacao.setDataCriacao(LocalDateTime.now());

        Solicitacao salva = solicitacaoRepository.save(solicitacao);
        notificacaoService.publicarSolicitacaoCriada(
                "Nova solicitacao #" + salva.getId() + " para o servico " + servico.getNome()
        );

        return toResponse(salva);
    }

    public List<SolicitacaoResponse> listar() {
        return solicitacaoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Solicitacao buscarEntidade(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitacao nao encontrada."));
    }

    public SolicitacaoResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public SolicitacaoResponse aceitar(Long solicitacaoId, Long profissionalId) {
        Solicitacao solicitacao = buscarEntidade(solicitacaoId);
        Profissional profissional = profissionalService.buscarEntidade(profissionalId);

        if (!Boolean.TRUE.equals(profissional.getDisponivel())) {
            throw new IllegalArgumentException("Profissional indisponivel para aceitar a solicitacao.");
        }
        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new IllegalArgumentException("Apenas solicitacoes pendentes podem ser aceitas.");
        }

        solicitacao.setProfissional(profissional);
        solicitacao.setStatus(StatusSolicitacao.ACEITA);
        profissionalService.atualizarDisponibilidade(profissional, false);
        solicitacaoRepository.save(solicitacao);

        notificacaoService.publicarSolicitacaoAceita(
                "Solicitacao #" + solicitacao.getId() + " aceita por " + profissional.getNome()
        );

        return toResponse(solicitacao);
    }

    public SolicitacaoResponse concluir(Long solicitacaoId) {
        Solicitacao solicitacao = buscarEntidade(solicitacaoId);
        if (solicitacao.getStatus() != StatusSolicitacao.ACEITA) {
            throw new IllegalArgumentException("Apenas solicitacoes aceitas podem ser concluidas.");
        }

        solicitacao.setStatus(StatusSolicitacao.CONCLUIDA);
        if (solicitacao.getProfissional() != null) {
            profissionalService.atualizarDisponibilidade(solicitacao.getProfissional(), true);
        }
        solicitacaoRepository.save(solicitacao);
        return toResponse(solicitacao);
    }

    public SolicitacaoResponse toResponse(Solicitacao solicitacao) {
        return new SolicitacaoResponse(
                solicitacao.getId(),
                solicitacao.getCliente().getId(),
                solicitacao.getCliente().getNome(),
                solicitacao.getProfissional() != null ? solicitacao.getProfissional().getId() : null,
                solicitacao.getProfissional() != null ? solicitacao.getProfissional().getNome() : null,
                solicitacao.getServico().getId(),
                solicitacao.getServico().getNome(),
                solicitacao.getStatus().name(),
                solicitacao.getEnderecoAtendimento(),
                solicitacao.getObservacoes(),
                solicitacao.getValorTotal(),
                solicitacao.getDataCriacao()
        );
    }
}
