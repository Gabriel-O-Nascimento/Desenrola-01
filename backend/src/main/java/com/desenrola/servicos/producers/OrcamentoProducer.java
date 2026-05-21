package com.desenrola.servicos.producers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrcamentoProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(OrcamentoEnviadoEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_ORCAMENTO_ENVIADO, evento);
    }
}
