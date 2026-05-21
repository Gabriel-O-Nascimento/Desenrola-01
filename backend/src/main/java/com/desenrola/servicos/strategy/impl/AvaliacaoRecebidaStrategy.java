package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class AvaliacaoRecebidaStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.AVALIACAO_RECEBIDA;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Você recebeu uma avaliação";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "O cliente avaliou seu serviço com nota " + contexto.getNota() + ".";
    }
}
