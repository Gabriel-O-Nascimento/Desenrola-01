package com.desenrola.servicos;

import com.desenrola.repositorios.NotificacaoRepository;
import com.desenrola.repositorios.entidades.Notificacao;
import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final List<NotificacaoStrategy> estrategias;

    private final Map<TipoNotificacao, NotificacaoStrategy> mapaEstrategias = new EnumMap<>(TipoNotificacao.class);

    @PostConstruct
    void registrarEstrategias() {
        estrategias.forEach(s -> mapaEstrategias.put(s.getTipo(), s));
    }

    @Transactional
    public Notificacao notificar(Long idUsuario, TipoNotificacao tipo,
                                  ContextoNotificacao contexto,
                                  Long idReferencia, String tipoReferencia) {
        NotificacaoStrategy estrategia = mapaEstrategias.getOrDefault(
                tipo, mapaEstrategias.get(TipoNotificacao.SISTEMA)
        );

        Notificacao notificacao = Notificacao.builder()
                .idUsuario(idUsuario)
                .tipo(tipo)
                .titulo(estrategia.montarTitulo(contexto))
                .mensagem(estrategia.montarMensagem(contexto))
                .lida(false)
                .idReferencia(idReferencia)
                .tipoReferencia(tipoReferencia)
                .criadoEm(LocalDateTime.now())
                .build();

        Notificacao salva = notificacaoRepository.save(notificacao);
        messagingTemplate.convertAndSend("/topic/notificacoes/" + idUsuario, salva);
        return salva;
    }
}
