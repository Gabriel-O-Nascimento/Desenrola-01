package com.desenrola.servicos;

import com.desenrola.servicos.eventos.AvaliacaoCriadaEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.AvaliacaoProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvaliacaoServiceTest {

    @Mock private AvaliacaoProducer avaliacaoProducer;
    @Mock private EventoFactory eventoFactory;

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Test
    void deveCriarAvaliacaoViaProducer() {
        AvaliacaoCriadaEvento evento = new AvaliacaoCriadaEvento();
        evento.setAvaliacaoId(1L);
        evento.setSolicitacaoId(2L);
        evento.setProfissionalId(5L);
        evento.setClienteId(10L);
        evento.setNota(5);
        evento.setComentario("Ótimo");

        when(eventoFactory.criarAvaliacaoCriada(1L, 2L, 5L, 10L, 5, "Ótimo"))
                .thenReturn(evento);

        avaliacaoService.criarAvaliacao(1L, 2L, 5L, 10L, 5, "Ótimo");

        verify(avaliacaoProducer).publicar(evento);
    }
}
