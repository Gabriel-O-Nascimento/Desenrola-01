package com.desenrola.servicos.strategy;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.impl.AvaliacaoRecebidaStrategy;
import com.desenrola.servicos.strategy.impl.NovaSolicitacaoStrategy;
import com.desenrola.servicos.strategy.impl.OrcamentoRecebidoStrategy;
import com.desenrola.servicos.strategy.impl.ServicoConcluidoStrategy;
import com.desenrola.servicos.strategy.impl.SistemaStrategy;
import com.desenrola.servicos.strategy.impl.SolicitacaoAceitaStrategy;
import com.desenrola.servicos.strategy.impl.SolicitacaoRecusadaStrategy;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class StrategiesTest {

    @Test
    void novaSolicitacaoStrategyMontaTituloEMensagem() {
        NovaSolicitacaoStrategy s = new NovaSolicitacaoStrategy();
        ContextoNotificacao ctx = ContextoNotificacao.builder().categoria("Hidráulica").build();

        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.NOVA_SOLICITACAO);
        assertThat(s.montarTitulo(ctx)).isEqualTo("Solicitação criada");
        assertThat(s.montarMensagem(ctx)).contains("Hidráulica");
    }

    @Test
    void orcamentoRecebidoStrategyIncluiValor() {
        OrcamentoRecebidoStrategy s = new OrcamentoRecebidoStrategy();
        ContextoNotificacao ctx = ContextoNotificacao.builder().valor(new BigDecimal("150.00")).build();

        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.ORCAMENTO_RECEBIDO);
        assertThat(s.montarMensagem(ctx)).contains("150.00");
    }

    @Test
    void solicitacaoAceitaStrategy() {
        SolicitacaoAceitaStrategy s = new SolicitacaoAceitaStrategy();
        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.SOLICITACAO_ACEITA);
        assertThat(s.montarTitulo(ContextoNotificacao.builder().build()))
                .isEqualTo("Solicitação aceita");
    }

    @Test
    void solicitacaoRecusadaStrategy() {
        SolicitacaoRecusadaStrategy s = new SolicitacaoRecusadaStrategy();
        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.SOLICITACAO_RECUSADA);
    }

    @Test
    void servicoConcluidoStrategy() {
        ServicoConcluidoStrategy s = new ServicoConcluidoStrategy();
        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.SERVICO_CONCLUIDO);
    }

    @Test
    void avaliacaoRecebidaStrategyIncluiNota() {
        AvaliacaoRecebidaStrategy s = new AvaliacaoRecebidaStrategy();
        ContextoNotificacao ctx = ContextoNotificacao.builder().nota(5).build();

        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.AVALIACAO_RECEBIDA);
        assertThat(s.montarMensagem(ctx)).contains("5");
    }

    @Test
    void sistemaStrategyComStatusMostraTransicao() {
        SistemaStrategy s = new SistemaStrategy();
        ContextoNotificacao ctx = ContextoNotificacao.builder()
                .statusAnterior("PENDENTE").statusNovo("APROVADA").build();

        assertThat(s.getTipo()).isEqualTo(TipoNotificacao.SISTEMA);
        assertThat(s.montarMensagem(ctx)).contains("PENDENTE").contains("APROVADA");
    }

    @Test
    void sistemaStrategySemStatusMostraGenerico() {
        SistemaStrategy s = new SistemaStrategy();
        assertThat(s.montarMensagem(ContextoNotificacao.builder().build()))
                .isEqualTo("Você tem uma atualização.");
    }
}
