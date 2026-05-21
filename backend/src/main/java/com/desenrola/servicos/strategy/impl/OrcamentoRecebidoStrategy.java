package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class OrcamentoRecebidoStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.ORCAMENTO_RECEBIDO;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Novo orçamento recebido";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        return "Você recebeu um orçamento de R$ " + contexto.getValor()
                + ". Confira e aprove ou recuse.";
    }
}
