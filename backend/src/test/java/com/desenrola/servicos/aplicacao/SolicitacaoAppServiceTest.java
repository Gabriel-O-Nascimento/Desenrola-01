package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.AtualizarStatusDTO;
import com.desenrola.controladores.dto.CriarSolicitacaoDTO;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.ServicoRepository;
import com.desenrola.repositorios.SolicitacaoRepository;
import com.desenrola.repositorios.entidades.CategoriaServico;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.repositorios.entidades.Servico;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.repositorios.entidades.StatusSolicitacao;
import com.desenrola.servicos.SolicitacaoService;
import com.desenrola.servicos.StatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitacaoAppServiceTest {

    @Mock private SolicitacaoRepository solicitacaoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private ProfissionalRepository profissionalRepository;
    @Mock private ServicoRepository servicoRepository;
    @Mock private SolicitacaoService solicitacaoService;
    @Mock private StatusService statusService;

    @InjectMocks
    private SolicitacaoAppService solicitacaoAppService;

    @Test
    void deveCriarSolicitacaoComSucesso() {
        CriarSolicitacaoDTO dto = CriarSolicitacaoDTO.builder()
                .idCliente(1L)
                .idServico(2)
                .titulo("Conserto")
                .descricao("Torneira vazando")
                .enderecoAtendimento("Rua X")
                .cidadeAtendimento("Curitiba")
                .estadoAtendimento("PR")
                .valorEstimado(new BigDecimal("100.00"))
                .build();

        Cliente cliente = Cliente.builder().idUsuario(1L).build();
        CategoriaServico cat = CategoriaServico.builder().id(1).nome("Hidráulica").build();
        Servico servico = Servico.builder().id(2).categoria(cat).build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(servicoRepository.findById(2)).thenReturn(Optional.of(servico));
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(inv -> {
            Solicitacao s = inv.getArgument(0);
            s.setId(10L);
            return s;
        });

        Solicitacao resultado = solicitacaoAppService.criar(dto);

        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getStatus()).isEqualTo(StatusSolicitacao.AGUARDANDO_ORCAMENTO);
        assertThat(resultado.getTitulo()).isEqualTo("Conserto");
        verify(solicitacaoService).criarSolicitacao(10L, 1L, "Hidráulica", "Torneira vazando", "Rua X");
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        CriarSolicitacaoDTO dto = CriarSolicitacaoDTO.builder().idCliente(99L).idServico(1).build();
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitacaoAppService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    void deveAtualizarStatusComSucesso() {
        Cliente cliente = Cliente.builder().idUsuario(1L).build();
        Solicitacao solicitacao = Solicitacao.builder()
                .id(1L).cliente(cliente).status(StatusSolicitacao.AGUARDANDO_ORCAMENTO)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build();

        AtualizarStatusDTO dto = AtualizarStatusDTO.builder()
                .novoStatus(StatusSolicitacao.EM_ANDAMENTO).build();

        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Solicitacao resultado = solicitacaoAppService.atualizarStatus(1L, dto);

        assertThat(resultado.getStatus()).isEqualTo(StatusSolicitacao.EM_ANDAMENTO);
        verify(statusService).atualizarStatus(anyLong(), anyLong(), any(), anyString(), anyString());
    }

    @Test
    void deveBuscarPorId() {
        Solicitacao s = Solicitacao.builder().id(5L).titulo("Teste").build();
        when(solicitacaoRepository.findById(5L)).thenReturn(Optional.of(s));

        assertThat(solicitacaoAppService.buscarPorId(5L).getTitulo()).isEqualTo("Teste");
    }

    @Test
    void deveListarPorCliente() {
        Solicitacao s = Solicitacao.builder().id(1L).build();
        when(solicitacaoRepository.findByClienteIdUsuarioOrderByCriadoEmDesc(10L))
                .thenReturn(List.of(s));

        assertThat(solicitacaoAppService.listarPorCliente(10L)).hasSize(1);
    }
}
