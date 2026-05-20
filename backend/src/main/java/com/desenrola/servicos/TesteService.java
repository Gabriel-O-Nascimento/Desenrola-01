package com.desenrola.servicos;

import com.desenrola.config.RabbitMQConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TesteService {

    private final RabbitTemplate rabbitTemplate;

    public TesteService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensagem(String mensagem) {
        try {
            System.out.println("Enviando mensagem para RabbitMQ: " + mensagem);
            rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_TESTE, mensagem);
        } catch (AmqpException ex) {
            System.out.println("RabbitMQ indisponivel. Mensagem nao enviada: " + mensagem);
        }
    }
}
