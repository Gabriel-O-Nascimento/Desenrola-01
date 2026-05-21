package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CriarOrcamentoDTO;
import com.desenrola.repositorios.OrcamentoRepository;
import com.desenrola.repositorios.SolicitacaoRepository;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Orcamento;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.repositorios.entidades.StatusOrcamento;
import com.desenrola.repositorios.entidades.StatusSolicitacao;
import com.desenrola.servicos.OrcamentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrcamentoAppServiceTest {

    @Mock private OrcamentoRepository orcamentoRepository;
    @Mock private SolicitacaoRepository solicitacaoRepository;
    @Mock private OrcamentoService orcamentoService;

    @InjectMocks
    private OrcamentoAppService orcamentoAppService;

    @Test
    void deveCriarOrcamentoComSucesso() {
        CriarOrcamentoDTO dto = CriarOrcamentoDTO.builder()
                .idSolicitacao(1L)
                .idProfissional(5L)
                .observacoes("Inclui material")
                .totalMaoObra(new BigDecimal("200.00"))
                .totalMateriais(new BigDecimal("100.00"))
                .build();

        Cliente cliente = Cliente.builder().idUsuario(10L).build();
        Solicitacao solicitacao = Solicitacao.builder()
                .id(1L).cliente(cliente).status(StatusSolicitacao.AGUARDANDO_ORCAMENTO)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build();

        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(inv -> {
            Orcamento o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Orcamento resultado = orcamentoAppService.criar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTotalGeral()).isEqualByComparingTo("300.00");
        assertThat(resultado.getStatus()).isEqualTo(StatusOrcamento.PENDENTE);
        assertThat(solicitacao.getStatus()).isEqualTo(StatusSolicitacao.ORCAMENTO_ENVIADO);
        verify(orcamentoService).enviarOrcamento(1L, 5L, 10L, new BigDecimal("300.00"), "Inclui material");
    }

    @Test
    void deveLancarExcecaoQuandoSolicitacaoNaoEncontrada() {
        CriarOrcamentoDTO dto = CriarOrcamentoDTO.builder().idSolicitacao(99L).build();
        when(solicitacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orcamentoAppService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Solicitação não encontrada");
    }

    @Test
    void deveCalcularTotalComValoresNulos() {
        CriarOrcamentoDTO dto = CriarOrcamentoDTO.builder()
                .idSolicitacao(1L)
                .idProfissional(5L)
                .totalMaoObra(null)
                .totalMateriais(null)
                .build();

        Cliente cliente = Cliente.builder().idUsuario(10L).build();
        Solicitacao solicitacao = Solicitacao.builder()
                .id(1L).cliente(cliente).status(StatusSolicitacao.AGUARDANDO_ORCAMENTO)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build();

        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(orcamentoRepository.save(any(Orcamento.class))).thenAnswer(inv -> {
            Orcamento o = inv.getArgument(0);
            o.setId(2L);
            return o;
        });
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(inv -> inv.getArgument(0));

        Orcamento resultado = orcamentoAppService.criar(dto);

        assertThat(resultado.getTotalGeral()).isEqualByComparingTo("0");
    }
}
