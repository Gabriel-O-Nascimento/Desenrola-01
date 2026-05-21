package com.desenrola.servicos.factory;

import com.desenrola.servicos.eventos.AvaliacaoCriadaEvento;
import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import com.desenrola.servicos.eventos.StatusAtualizadoEvento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class EventoFactoryTest {

    private final EventoFactory factory = new EventoFactory();

    @Test
    void deveCriarSolicitacaoCriadaComDataPreenchida() {
        SolicitacaoCriadaEvento e = factory.criarSolicitacaoCriada(
                1L, 10L, "Hidráulica", "Vazamento", "Rua X"
        );

        assertThat(e.getSolicitacaoId()).isEqualTo(1L);
        assertThat(e.getClienteId()).isEqualTo(10L);
        assertThat(e.getCategoria()).isEqualTo("Hidráulica");
        assertThat(e.getDataCriacao()).isNotNull();
    }

    @Test
    void deveCriarOrcamentoEnviado() {
        OrcamentoEnviadoEvento e = factory.criarOrcamentoEnviado(
                1L, 5L, 10L, new BigDecimal("250.00"), "obs"
        );

        assertThat(e.getValor()).isEqualByComparingTo("250.00");
        assertThat(e.getDataEnvio()).isNotNull();
    }

    @Test
    void deveCriarStatusAtualizado() {
        StatusAtualizadoEvento e = factory.criarStatusAtualizado(
                1L, 10L, 5L, "PENDENTE", "APROVADA"
        );

        assertThat(e.getStatusAnterior()).isEqualTo("PENDENTE");
        assertThat(e.getStatusNovo()).isEqualTo("APROVADA");
    }

    @Test
    void deveCriarAvaliacaoCriada() {
        AvaliacaoCriadaEvento e = factory.criarAvaliacaoCriada(
                1L, 2L, 5L, 10L, 5, "Ótimo"
        );

        assertThat(e.getNota()).isEqualTo(5);
        assertThat(e.getComentario()).isEqualTo("Ótimo");
    }
}
