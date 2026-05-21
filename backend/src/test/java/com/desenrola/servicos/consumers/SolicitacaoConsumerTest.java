package com.desenrola.servicos.consumers;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.NotificacaoService;
import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SolicitacaoConsumerTest {

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private SolicitacaoConsumer solicitacaoConsumer;

    @Test
    void deveProcessarEventoENotificarCliente() {
        SolicitacaoCriadaEvento evento = new SolicitacaoCriadaEvento(
                1L, 10L, "Hidráulica", "Vazamento", "Rua X", LocalDateTime.now()
        );

        solicitacaoConsumer.consumir(evento);

        ArgumentCaptor<ContextoNotificacao> ctxCaptor = ArgumentCaptor.forClass(ContextoNotificacao.class);

        verify(notificacaoService, times(1)).notificar(
                eq(10L),
                eq(TipoNotificacao.NOVA_SOLICITACAO),
                ctxCaptor.capture(),
                eq(1L),
                eq("solicitacao")
        );

        assertThat(ctxCaptor.getValue().getCategoria()).isEqualTo("Hidráulica");
    }
}
