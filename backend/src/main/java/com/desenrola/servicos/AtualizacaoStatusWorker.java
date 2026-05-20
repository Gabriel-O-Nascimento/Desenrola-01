package com.desenrola.servicos;

import com.desenrola.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AtualizacaoStatusWorker {

    @RabbitListener(queues = RabbitMQConfig.FILA_SOLICITACAO_ACEITA)
    public void processar(String mensagem) {
        System.out.println("Worker de atualizacao de status: " + mensagem);
    }
}
