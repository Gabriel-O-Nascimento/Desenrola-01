package com.desenrola.servicos.consumers;

import com.desenrola.config.RabbitMQConfig;
import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.NotificacaoService;
import com.desenrola.servicos.eventos.AvaliacaoCriadaEvento;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AvaliacaoConsumer {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = RabbitMQConfig.FILA_AVALIACAO_CRIADA)
    public void consumir(AvaliacaoCriadaEvento evento) {
        log.info("Avaliação criada: solicitacao={}, profissional={}, nota={}",
                evento.getSolicitacaoId(), evento.getProfissionalId(), evento.getNota());

        ContextoNotificacao contexto = ContextoNotificacao.builder()
                .nota(evento.getNota())
                .build();

        notificacaoService.notificar(
                evento.getProfissionalId(),
                TipoNotificacao.AVALIACAO_RECEBIDA,
                contexto,
                evento.getAvaliacaoId(),
                "avaliacao"
        );
    }
}
