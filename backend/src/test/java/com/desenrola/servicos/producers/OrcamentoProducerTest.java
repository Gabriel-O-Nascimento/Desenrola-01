package com.desenrola.servicos.producers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrcamentoProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrcamentoProducer orcamentoProducer;

    @Test
    void devePublicarEventoNaFilaCorreta() {
        OrcamentoEnviadoEvento evento = new OrcamentoEnviadoEvento(
                1L, 5L, 10L, new BigDecimal("150.00"), "Inclui materiais", LocalDateTime.now()
        );

        orcamentoProducer.publicar(evento);

        verify(rabbitTemplate, times(1))
                .convertAndSend(RabbitMQConfig.FILA_ORCAMENTO_ENVIADO, evento);
    }
}
