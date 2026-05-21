package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CriarOrcamentoDTO;
import com.desenrola.repositorios.OrcamentoRepository;
import com.desenrola.repositorios.SolicitacaoRepository;
import com.desenrola.repositorios.entidades.Orcamento;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.repositorios.entidades.StatusOrcamento;
import com.desenrola.repositorios.entidades.StatusSolicitacao;
import com.desenrola.servicos.OrcamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrcamentoAppService {

    private final OrcamentoRepository orcamentoRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final OrcamentoService orcamentoService;

    @Transactional
    public Orcamento criar(CriarOrcamentoDTO dto) {
        Solicitacao solicitacao = solicitacaoRepository.findById(dto.getIdSolicitacao())
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        BigDecimal maoObra = dto.getTotalMaoObra() != null ? dto.getTotalMaoObra() : BigDecimal.ZERO;
        BigDecimal materiais = dto.getTotalMateriais() != null ? dto.getTotalMateriais() : BigDecimal.ZERO;
        BigDecimal total = maoObra.add(materiais);

        LocalDateTime agora = LocalDateTime.now();

        Orcamento orcamento = Orcamento.builder()
                .solicitacao(solicitacao)
                .observacoes(dto.getObservacoes())
                .totalMaoObra(maoObra)
                .totalMateriais(materiais)
                .totalGeral(total)
                .status(StatusOrcamento.PENDENTE)
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        Orcamento salvo = orcamentoRepository.save(orcamento);

        solicitacao.setStatus(StatusSolicitacao.ORCAMENTO_ENVIADO);
        solicitacao.setAtualizadoEm(agora);
        solicitacaoRepository.save(solicitacao);

        orcamentoService.enviarOrcamento(
                solicitacao.getId(),
                dto.getIdProfissional(),
                solicitacao.getCliente().getIdUsuario(),
                total,
                dto.getObservacoes()
        );

        return salvo;
    }
}
