package com.desenrola.servicos;

import com.desenrola.config.RabbitMQConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final RabbitTemplate rabbitTemplate;

    public NotificacaoService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarSolicitacaoCriada(String mensagem) {
        publicar(RabbitMQConfig.FILA_SOLICITACAO_CRIADA, mensagem);
    }

    public void publicarSolicitacaoAceita(String mensagem) {
        publicar(RabbitMQConfig.FILA_SOLICITACAO_ACEITA, mensagem);
    }

    private void publicar(String fila, String mensagem) {
        try {
            rabbitTemplate.convertAndSend(fila, mensagem);
        } catch (AmqpException ex) {
            System.out.println("RabbitMQ indisponivel. Evento nao publicado: " + mensagem);
        }
    }
}
