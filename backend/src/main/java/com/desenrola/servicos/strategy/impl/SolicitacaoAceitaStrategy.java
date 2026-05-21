package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class SolicitacaoAceitaStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.SOLICITACAO_ACEITA;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Solicitação aceita";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "Um profissional aceitou sua solicitação. Acompanhe o andamento.";
    }
}
