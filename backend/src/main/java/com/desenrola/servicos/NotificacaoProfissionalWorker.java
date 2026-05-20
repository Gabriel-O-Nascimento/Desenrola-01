package com.desenrola.servicos;

import com.desenrola.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoProfissionalWorker {

    @RabbitListener(queues = RabbitMQConfig.FILA_SOLICITACAO_CRIADA)
    public void processar(String mensagem) {
        System.out.println("Worker de notificacao: " + mensagem);
    }
}
