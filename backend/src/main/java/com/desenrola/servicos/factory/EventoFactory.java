package com.desenrola.servicos.factory;

import com.desenrola.servicos.eventos.AvaliacaoCriadaEvento;
import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import com.desenrola.servicos.eventos.StatusAtualizadoEvento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class EventoFactory {

    public SolicitacaoCriadaEvento criarSolicitacaoCriada(Long solicitacaoId, Long clienteId,
                                                          String categoria, String descricao,
                                                          String endereco) {
        return new SolicitacaoCriadaEvento(
                solicitacaoId, clienteId, categoria, descricao, endereco, LocalDateTime.now()
        );
    }

    public OrcamentoEnviadoEvento criarOrcamentoEnviado(Long solicitacaoId, Long profissionalId,
                                                        Long clienteId, BigDecimal valor,
                                                        String descricao) {
        return new OrcamentoEnviadoEvento(
                solicitacaoId, profissionalId, clienteId, valor, descricao, LocalDateTime.now()
        );
    }

    public StatusAtualizadoEvento criarStatusAtualizado(Long solicitacaoId, Long clienteId,
                                                        Long profissionalId, String statusAnterior,
                                                        String statusNovo) {
        return new StatusAtualizadoEvento(
                solicitacaoId, clienteId, profissionalId,
                statusAnterior, statusNovo, LocalDateTime.now()
        );
    }

    public AvaliacaoCriadaEvento criarAvaliacaoCriada(Long avaliacaoId, Long solicitacaoId,
                                                      Long profissionalId, Long clienteId,
                                                      Integer nota, String comentario) {
        return new AvaliacaoCriadaEvento(
                avaliacaoId, solicitacaoId, profissionalId, clienteId,
                nota, comentario, LocalDateTime.now()
        );
    }
}
