package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CriarAvaliacaoDTO;
import com.desenrola.repositorios.AvaliacaoRepository;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.SolicitacaoRepository;
import com.desenrola.repositorios.entidades.Avaliacao;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.servicos.AvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AvaliacaoAppService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final AvaliacaoService avaliacaoService;

    @Transactional
    public Avaliacao criar(CriarAvaliacaoDTO dto) {
        if (dto.getNota() == null || dto.getNota() < 1 || dto.getNota() > 5) {
            throw new IllegalArgumentException("A nota deve estar entre 1 e 5.");
        }

        Solicitacao solicitacao = solicitacaoRepository.findById(dto.getIdSolicitacao())
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));

        Profissional profissional = profissionalRepository.findById(dto.getIdProfissional())
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado."));

        if (avaliacaoRepository.findBySolicitacaoId(solicitacao.getId()).isPresent()) {
            throw new IllegalStateException("Esta solicitação já foi avaliada.");
        }

        LocalDateTime agora = LocalDateTime.now();

        Avaliacao avaliacao = Avaliacao.builder()
                .solicitacao(solicitacao)
                .cliente(cliente)
                .profissional(profissional)
                .nota(dto.getNota())
                .comentario(dto.getComentario())
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        Avaliacao salva = avaliacaoRepository.save(avaliacao);

        avaliacaoService.criarAvaliacao(
                salva.getId(),
                solicitacao.getId(),
                profissional.getIdUsuario(),
                cliente.getIdUsuario(),
                dto.getNota(),
                dto.getComentario()
        );

        return salva;
    }
}
