package com.desenrola.servicos;

import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.OrcamentoProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrcamentoService {

    private final OrcamentoProducer orcamentoProducer;
    private final EventoFactory eventoFactory;

    public void enviarOrcamento(Long solicitacaoId, Long profissionalId, Long clienteId,
                                 BigDecimal valor, String descricao) {
        OrcamentoEnviadoEvento evento = eventoFactory.criarOrcamentoEnviado(
                solicitacaoId, profissionalId, clienteId, valor, descricao
        );
        orcamentoProducer.publicar(evento);
    }
}
