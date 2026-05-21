package com.desenrola.servicos.strategy;

import com.desenrola.repositorios.entidades.TipoNotificacao;

public interface NotificacaoStrategy {

    TipoNotificacao getTipo();

    String montarTitulo(ContextoNotificacao contexto);

    String montarMensagem(ContextoNotificacao contexto);
}
