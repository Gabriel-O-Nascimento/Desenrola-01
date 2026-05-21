package com.desenrola.servicos.consumers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.NotificacaoService;
import com.desenrola.servicos.eventos.StatusAtualizadoEvento;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatusConsumer {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.FILA_STATUS_ATUALIZADO)
    public void consumir(StatusAtualizadoEvento evento) {
        log.info("Status atualizado: solicitacao={}, de={} para={}",
                evento.getSolicitacaoId(), evento.getStatusAnterior(), evento.getStatusNovo());

        TipoNotificacao tipo = mapearTipo(evento.getStatusNovo());

        ContextoNotificacao contexto = ContextoNotificacao.builder()
                .statusAnterior(evento.getStatusAnterior())
                .statusNovo(evento.getStatusNovo())
                .build();

        notificacaoService.notificar(
                evento.getClienteId(),
                tipo,
                contexto,
                evento.getSolicitacaoId(),
                "solicitacao"
        );
    }

    private TipoNotificacao mapearTipo(String statusNovo) {
        return switch (statusNovo) {
            case "APROVADA" -> TipoNotificacao.SOLICITACAO_ACEITA;
            case "RECUSADA" -> TipoNotificacao.SOLICITACAO_RECUSADA;
            case "CONCLUIDA" -> TipoNotificacao.SERVICO_CONCLUIDO;
            default -> TipoNotificacao.SISTEMA;
        };
    }
}
