package com.desenrola.servicos;

import com.desenrola.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TesteWorker {
    
    @RabbitListener(queues = RabbitMQConfig.FILA_TESTE)
    public void processarMensagem(String mensagem) {
        System.out.println("📥 Worker recebeu mensagem: " + mensagem);
        System.out.println("✅ Mensagem processada com sucesso!");
        
        // Aqui você pode adicionar lógica de processamento
        // Por exemplo: enviar notificação, atualizar banco, etc.
    }
}
