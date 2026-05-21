package com.desenrola.servicos;

import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.SolicitacaoProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoProducer solicitacaoProducer;

    @Mock
    private EventoFactory eventoFactory;

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    @Test
    void deveCriarEventoEPublicarComDadosCorretos() {
        SolicitacaoCriadaEvento evento = new SolicitacaoCriadaEvento(
                1L, 10L, "Hidráulica", "Vazamento na cozinha", "Rua das Flores, 123", LocalDateTime.now()
        );

        when(eventoFactory.criarSolicitacaoCriada(1L, 10L, "Hidráulica", "Vazamento na cozinha", "Rua das Flores, 123"))
                .thenReturn(evento);

        solicitacaoService.criarSolicitacao(
                1L, 10L, "Hidráulica", "Vazamento na cozinha", "Rua das Flores, 123"
        );

        verify(solicitacaoProducer).publicar(evento);
    }
}
