package com.desenrola.servicos.producers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SolicitacaoProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SolicitacaoProducer solicitacaoProducer;

    @Test
    void devePublicarEventoNaFilaCorreta() {
        SolicitacaoCriadaEvento evento = new SolicitacaoCriadaEvento(
                1L, 10L, "Hidráulica", "Vazamento", "Rua X", LocalDateTime.now()
        );

        solicitacaoProducer.publicar(evento);

        verify(rabbitTemplate, times(1))
                .convertAndSend(RabbitMQConfig.FILA_SOLICITACAO_CRIADA, evento);
    }
}
