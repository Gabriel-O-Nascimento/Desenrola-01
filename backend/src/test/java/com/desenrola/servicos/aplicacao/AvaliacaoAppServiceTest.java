package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CriarAvaliacaoDTO;
import com.desenrola.repositorios.AvaliacaoRepository;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.SolicitacaoRepository;
import com.desenrola.repositorios.entidades.Avaliacao;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.servicos.AvaliacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvaliacaoAppServiceTest {

    @Mock private AvaliacaoRepository avaliacaoRepository;
    @Mock private SolicitacaoRepository solicitacaoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ProfissionalRepository profissionalRepository;
    @Mock private AvaliacaoService avaliacaoService;

    @InjectMocks
    private AvaliacaoAppService avaliacaoAppService;

    @Test
    void deveCriarAvaliacaoComSucesso() {
        CriarAvaliacaoDTO dto = CriarAvaliacaoDTO.builder()
                .idSolicitacao(1L)
                .idCliente(10L)
                .idProfissional(5L)
                .nota(5)
                .comentario("Ótimo serviço")
                .build();

        Solicitacao solicitacao = Solicitacao.builder().id(1L).build();
        Cliente cliente = Cliente.builder().idUsuario(10L).build();
        Profissional profissional = Profissional.builder().idUsuario(5L).build();

        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(clienteRepository.findById(10L)).thenReturn(Optional.of(cliente));
        when(profissionalRepository.findById(5L)).thenReturn(Optional.of(profissional));
        when(avaliacaoRepository.findBySolicitacaoId(1L)).thenReturn(Optional.empty());
        when(avaliacaoRepository.save(any(Avaliacao.class))).thenAnswer(inv -> {
            Avaliacao a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        Avaliacao resultado = avaliacaoAppService.criar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNota()).isEqualTo(5);
        verify(avaliacaoService).criarAvaliacao(1L, 1L, 5L, 10L, 5, "Ótimo serviço");
    }

    @Test
    void deveLancarExcecaoQuandoNotaInvalida() {
        CriarAvaliacaoDTO dto = CriarAvaliacaoDTO.builder().nota(6).build();

        assertThatThrownBy(() -> avaliacaoAppService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nota");
    }

    @Test
    void deveLancarExcecaoQuandoNotaNula() {
        CriarAvaliacaoDTO dto = CriarAvaliacaoDTO.builder().nota(null).build();

        assertThatThrownBy(() -> avaliacaoAppService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nota");
    }

    @Test
    void deveLancarExcecaoQuandoJaAvaliada() {
        CriarAvaliacaoDTO dto = CriarAvaliacaoDTO.builder()
                .idSolicitacao(1L).idCliente(10L).idProfissional(5L).nota(4).build();

        Solicitacao solicitacao = Solicitacao.builder().id(1L).build();
        Cliente cliente = Cliente.builder().idUsuario(10L).build();
        Profissional profissional = Profissional.builder().idUsuario(5L).build();

        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(clienteRepository.findById(10L)).thenReturn(Optional.of(cliente));
        when(profissionalRepository.findById(5L)).thenReturn(Optional.of(profissional));
        when(avaliacaoRepository.findBySolicitacaoId(1L))
                .thenReturn(Optional.of(Avaliacao.builder().id(99L).build()));

        assertThatThrownBy(() -> avaliacaoAppService.criar(dto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("já foi avaliada");
    }
}
