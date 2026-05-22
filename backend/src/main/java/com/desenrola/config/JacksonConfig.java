package com.desenrola.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura o Jackson para serializar entidades JPA com associacoes lazy
 * sem disparar erro de Hibernate ByteBuddyInterceptor. Os campos lazy nao
 * carregados retornam como null no JSON.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Module hibernateModule() {
        Hibernate6Module module = new Hibernate6Module();
        module.disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
        return module;
    }
}
