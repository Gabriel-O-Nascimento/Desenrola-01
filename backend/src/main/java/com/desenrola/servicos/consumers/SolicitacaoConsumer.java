package com.desenrola.servicos.consumers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.NotificacaoService;
import com.desenrola.servicos.eventos.SolicitacaoCriadaEvento;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SolicitacaoConsumer {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.FILA_SOLICITACAO_CRIADA)
    public void consumir(SolicitacaoCriadaEvento evento) {
        log.info("Solicitação recebida: id={}, categoria={}, cliente={}",
                evento.getSolicitacaoId(), evento.getCategoria(), evento.getClienteId());

        ContextoNotificacao contexto = ContextoNotificacao.builder()
                .categoria(evento.getCategoria())
                .build();

        notificacaoService.notificar(
                evento.getClienteId(),
                TipoNotificacao.NOVA_SOLICITACAO,
                contexto,
                evento.getSolicitacaoId(),
                "solicitacao"
        );
    }
}
