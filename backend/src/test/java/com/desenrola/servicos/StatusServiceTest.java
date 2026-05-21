package com.desenrola.servicos;

import com.desenrola.servicos.eventos.StatusAtualizadoEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.StatusProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock private StatusProducer statusProducer;
    @Mock private EventoFactory eventoFactory;

    @InjectMocks
    private StatusService statusService;

    @Test
    void deveAtualizarStatusViaProducer() {
        StatusAtualizadoEvento evento = new StatusAtualizadoEvento();
        evento.setSolicitacaoId(1L);
        evento.setClienteId(10L);
        evento.setProfissionalId(5L);
        evento.setStatusAnterior("PENDENTE");
        evento.setStatusNovo("APROVADA");

        when(eventoFactory.criarStatusAtualizado(1L, 10L, 5L, "PENDENTE", "APROVADA"))
                .thenReturn(evento);

        statusService.atualizarStatus(1L, 10L, 5L, "PENDENTE", "APROVADA");

        verify(statusProducer).publicar(evento);
    }
}
