package com.desenrola.servicos.consumers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.NotificacaoService;
import com.desenrola.servicos.eventos.OrcamentoEnviadoEvento;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrcamentoConsumer {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.FILA_ORCAMENTO_ENVIADO)
    public void consumir(OrcamentoEnviadoEvento evento) {
        log.info("Orçamento enviado: solicitacao={}, profissional={}, valor={}",
                evento.getSolicitacaoId(), evento.getProfissionalId(), evento.getValor());

        ContextoNotificacao contexto = ContextoNotificacao.builder()
                .valor(evento.getValor())
                .build();

        notificacaoService.notificar(
                evento.getClienteId(),
                TipoNotificacao.ORCAMENTO_RECEBIDO,
                contexto,
                evento.getSolicitacaoId(),
                "solicitacao"
        );
    }
}
