package com.desenrola.servicos;

import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import com.desenrola.servicos.factory.EventoFactory;
import com.desenrola.servicos.producers.OrcamentoProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoServiceTest {

    @Mock private OrcamentoProducer orcamentoProducer;
    @Mock private EventoFactory eventoFactory;

    @InjectMocks
    private OrcamentoService orcamentoService;

    @Test
    void deveEnviarOrcamentoViaProducer() {
        OrcamentoEnviadoEvento evento = new OrcamentoEnviadoEvento();
        evento.setSolicitacaoId(1L);
        evento.setProfissionalId(5L);
        evento.setClienteId(10L);
        evento.setValor(new BigDecimal("250.00"));
        evento.setDescricao("obs");

        when(eventoFactory.criarOrcamentoEnviado(1L, 5L, 10L, new BigDecimal("250.00"), "obs"))
                .thenReturn(evento);

        orcamentoService.enviarOrcamento(1L, 5L, 10L, new BigDecimal("250.00"), "obs");

        verify(orcamentoProducer).publicar(evento);
    }
}
