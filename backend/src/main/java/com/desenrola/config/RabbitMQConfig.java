package com.desenrola.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String FILA_TESTE = "fila.teste";
    
    @Bean
    public Queue filaTeste() {
        return new Queue(FILA_TESTE, true);
    }
}
