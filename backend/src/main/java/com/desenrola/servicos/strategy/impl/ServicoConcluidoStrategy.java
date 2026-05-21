package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class ServicoConcluidoStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.SERVICO_CONCLUIDO;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Serviço concluído";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "Seu serviço foi finalizado. Não esqueça de avaliar o profissional!";
    }
}
