package com.desenrola.servicos.producers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.servicos.eventos.AvaliacaoCriadaEvento;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvaliacaoProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(AvaliacaoCriadaEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_AVALIACAO_CRIADA, evento);
    }
}
