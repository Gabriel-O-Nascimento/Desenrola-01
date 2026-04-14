package com.desenrola.servicos;

import com.desenrola.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TesteService {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void enviarMensagem(String mensagem) {
        System.out.println("📤 Enviando mensagem para RabbitMQ: " + mensagem);
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_TESTE, mensagem);
    }
}
