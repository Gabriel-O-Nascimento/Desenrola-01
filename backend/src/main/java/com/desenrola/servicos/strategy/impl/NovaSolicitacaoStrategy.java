package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class NovaSolicitacaoStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.NOVA_SOLICITACAO;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Solicitação criada";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "Sua solicitação de " + contexto.getCategoria()
                + " foi registrada e profissionais serão notificados.";
    }
}
