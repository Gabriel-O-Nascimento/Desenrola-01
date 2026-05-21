package com.desenrola.servicos.strategy.impl;

import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import org.springframework.stereotype.Component;

@Component
public class SistemaStrategy implements NotificacaoStrategy {

    @Override
    public TipoNotificacao getTipo() {
        return TipoNotificacao.SISTEMA;
    }

    @Override
    public String montarTitulo(ContextoNotificacao contexto) {
        return "Atualização do sistema";
    }

    @Override
    public String montarMensagem(ContextoNotificacao contexto) {
        if (contexto.getStatusAnterior() != null && contexto.getStatusNovo() != null) {
            return "Sua solicitação mudou de " + contexto.getStatusAnterior()
                    + " para " + contexto.getStatusNovo() + ".";
        }
        return "Você tem uma atualização.";
    }
}
