package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoRecusadaStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.SOLICITACAO_RECUSADA;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Solicitação recusada";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "Sua solicitação foi recusada. Tente outro profissional.";
    }
}
