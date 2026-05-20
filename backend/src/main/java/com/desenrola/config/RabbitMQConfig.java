package com.desenrola.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_TESTE = "fila.teste";
    public static final String FILA_SOLICITACAO_CRIADA = "fila.solicitacao.criada";
    public static final String FILA_SOLICITACAO_ACEITA = "fila.solicitacao.aceita";

    @Bean
    public Queue filaTeste() {
        return new Queue(FILA_TESTE, true);
    }

    @Bean
    public Queue filaSolicitacaoCriada() {
        return new Queue(FILA_SOLICITACAO_CRIADA, true);
    }

    @Bean
    public Queue filaSolicitacaoAceita() {
        return new Queue(FILA_SOLICITACAO_ACEITA, true);
    }
}
