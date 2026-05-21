package com.desenrola.servicos.producers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.servicos.eventos.StatusAtualizadoEvento;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatusProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(StatusAtualizadoEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_STATUS_ATUALIZADO, evento);
    }
}
