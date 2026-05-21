package com.desenrola.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_SOLICITACAO_CRIADA = "fila.solicitacao.criada";
    public static final String FILA_ORCAMENTO_ENVIADO = "fila.orcamento.enviado";
    public static final String FILA_STATUS_ATUALIZADO = "fila.status.atualizado";
    public static final String FILA_AVALIACAO_CRIADA = "fila.avaliacao.criada";

    @Bean
    public Queue filaSolicitacaoCriada() {
        return new Queue(FILA_SOLICITACAO_CRIADA, true);
    }

    @Bean
    public Queue filaOrcamentoEnviado() {
        return new Queue(FILA_ORCAMENTO_ENVIADO, true);
    }

    @Bean
    public Queue filaStatusAtualizado() {
        return new Queue(FILA_STATUS_ATUALIZADO, true);
    }

    @Bean
    public Queue filaAvaliacaoCriada() {
        return new Queue(FILA_AVALIACAO_CRIADA, true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
