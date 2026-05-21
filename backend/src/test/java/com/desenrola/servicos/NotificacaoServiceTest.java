package com.desenrola.servicos;

import com.desenrola.repositorios.NotificacaoRepository;
import com.desenrola.repositorios.entidades.Notificacao;
import com.desenrola.repositorios.entidades.TipoNotificacao;
import com.desenrola.servicos.strategy.ContextoNotificacao;
import com.desenrola.servicos.strategy.NotificacaoStrategy;
import com.desenrola.servicos.strategy.impl.NovaSolicitacaoStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    private NotificacaoService notificacaoService;

    @BeforeEach
    void setUp() {
        List<NotificacaoStrategy> estrategias = List.of(new NovaSolicitacaoStrategy());
        notificacaoService = new NotificacaoService(
                notificacaoRepository, messagingTemplate, estrategias
        );
        notificacaoService.registrarEstrategias();
    }

    @Test
    void deveSalvarNotificacaoEEnviarPorWebSocket() {
        Notificacao salvaSimulada = Notificacao.builder()
                .id(99L)
                .idUsuario(10L)
                .tipo(TipoNotificacao.NOVA_SOLICITACAO)
                .titulo("Solicitação criada")
                .mensagem("Sua solicitação na categoria Hidráulica foi criada com sucesso.")
                .lida(false)
                .idReferencia(1L)
                .tipoReferencia("solicitacao")
                .criadoEm(LocalDateTime.now())
                .build();

        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(salvaSimulada);

        ContextoNotificacao contexto = ContextoNotificacao.builder()
                .categoria("Hidráulica")
                .build();

        Notificacao resultado = notificacaoService.notificar(
                10L, TipoNotificacao.NOVA_SOLICITACAO, contexto, 1L, "solicitacao"
        );

        ArgumentCaptor<Notificacao> captor = ArgumentCaptor.forClass(Notificacao.class);
        verify(notificacaoRepository, times(1)).save(captor.capture());

        Notificacao paraSalvar = captor.getValue();
        assertThat(paraSalvar.getIdUsuario()).isEqualTo(10L);
        assertThat(paraSalvar.getTipo()).isEqualTo(TipoNotificacao.NOVA_SOLICITACAO);
        assertThat(paraSalvar.getTitulo()).isEqualTo("Solicitação criada");
        assertThat(paraSalvar.getMensagem()).contains("Hidráulica");
        assertThat(paraSalvar.getLida()).isFalse();
        assertThat(paraSalvar.getCriadoEm()).isNotNull();

        verify(messagingTemplate, times(1))
                .convertAndSend(eq("/topic/notificacoes/10"), eq(salvaSimulada));

        assertThat(resultado.getId()).isEqualTo(99L);
    }
}
