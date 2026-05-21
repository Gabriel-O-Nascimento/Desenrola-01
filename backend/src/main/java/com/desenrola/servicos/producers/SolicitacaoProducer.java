package com.desenrola.servicos.producers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitacaoProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publicar(SolicitacaoCriadaEvento evento) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_SOLICITACAO_CRIADA, evento);
    }
}
