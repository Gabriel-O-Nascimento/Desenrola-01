package com.desenrola.servicos;

import com.desenrola.dto.AvaliacaoResponse;
import com.desenrola.dto.CriarAvaliacaoRequest;
import com.desenrola.modelos.Avaliacao;
import com.desenrola.modelos.Profissional;
import com.desenrola.modelos.Solicitacao;
import com.desenrola.modelos.enums.StatusSolicitacao;
import com.desenrola.repositorios.AvaliacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final SolicitacaoService solicitacaoService;
    private final ProfissionalService profissionalService;

    public AvaliacaoService(
            AvaliacaoRepository avaliacaoRepository,
            SolicitacaoService solicitacaoService,
            ProfissionalService profissionalService
    ) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.solicitacaoService = solicitacaoService;
        this.profissionalService = profissionalService;
    }

    public AvaliacaoResponse criar(CriarAvaliacaoRequest request) {
        Solicitacao solicitacao = solicitacaoService.buscarEntidade(request.solicitacaoId());

        if (solicitacao.getStatus() != StatusSolicitacao.CONCLUIDA) {
            throw new IllegalArgumentException("A avaliacao so pode ser criada apos a conclusao do servico.");
        }
        if (solicitacao.getProfissional() == null) {
            throw new IllegalArgumentException("Solicitacao sem profissional vinculado.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setSolicitacao(solicitacao);
        avaliacao.setCliente(solicitacao.getCliente());
        avaliacao.setProfissional(solicitacao.getProfissional());
        avaliacao.setNota(request.nota());
        avaliacao.setComentario(request.comentario());
        avaliacao.setCriadoEm(LocalDateTime.now());

        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        atualizarMediaProfissional(solicitacao.getProfissional());
        return toResponse(salva);
    }

    public List<AvaliacaoResponse> listarPorProfissional(Long profissionalId) {
        return avaliacaoRepository.findByProfissionalId(profissionalId).stream()
                .map(this::toResponse)
                .toList();
    }

    private void atualizarMediaProfissional(Profissional profissional) {
        List<Avaliacao> avaliacoes = avaliacaoRepository.findByProfissionalId(profissional.getId());
        double media = avaliacoes.stream().mapToInt(Avaliacao::getNota).average().orElse(0.0);
        profissionalService.atualizarAvaliacaoMedia(profissional, media);
    }

    private AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getSolicitacao().getId(),
                avaliacao.getCliente().getId(),
                avaliacao.getCliente().getNome(),
                avaliacao.getProfissional().getId(),
                avaliacao.getProfissional().getNome(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getCriadoEm()
        );
    }
}
