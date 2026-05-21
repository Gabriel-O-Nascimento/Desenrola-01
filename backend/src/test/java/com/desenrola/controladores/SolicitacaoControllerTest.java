package com.desenrola.controladores;

import com.desenrola.controladores.dto.AtualizarStatusDTO;
import com.desenrola.controladores.dto.CriarSolicitacaoDTO;
import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.repositorios.entidades.StatusSolicitacao;
import com.desenrola.servicos.aplicacao.SolicitacaoAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitacaoController.class)
class SolicitacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitacaoAppService solicitacaoAppService;

    @Test
    void deveCriarSolicitacaoComStatus201() throws Exception {
        CriarSolicitacaoDTO dto = CriarSolicitacaoDTO.builder()
                .idCliente(1L)
                .idServico(1)
                .titulo("Conserto de torneira")
                .descricao("Torneira vazando")
                .enderecoAtendimento("Rua X, 100")
                .cidadeAtendimento("Curitiba")
                .estadoAtendimento("PR")
                .valorEstimado(new BigDecimal("200.00"))
                .build();

        Solicitacao solicitacao = Solicitacao.builder()
                .id(1L)
                .titulo("Conserto de torneira")
                .status(StatusSolicitacao.AGUARDANDO_ORCAMENTO)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(solicitacaoAppService.criar(any(CriarSolicitacaoDTO.class))).thenReturn(solicitacao);

        mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Conserto de torneira"));
    }

    @Test
    void deveBuscarSolicitacaoPorId() throws Exception {
        Solicitacao solicitacao = Solicitacao.builder()
                .id(5L)
                .titulo("Pintura")
                .status(StatusSolicitacao.AGUARDANDO_ORCAMENTO)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(solicitacaoAppService.buscarPorId(5L)).thenReturn(solicitacao);

        mockMvc.perform(get("/api/solicitacoes/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.titulo").value("Pintura"));
    }

    @Test
    void deveListarPorCliente() throws Exception {
        Solicitacao s = Solicitacao.builder()
                .id(1L).titulo("Teste").status(StatusSolicitacao.PENDENTE)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build();

        when(solicitacaoAppService.listarPorCliente(10L)).thenReturn(List.of(s));

        mockMvc.perform(get("/api/solicitacoes").param("idCliente", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveListarPorProfissional() throws Exception {
        Solicitacao s = Solicitacao.builder()
                .id(2L).titulo("Elétrica").status(StatusSolicitacao.EM_ANDAMENTO)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build();

        when(solicitacaoAppService.listarPorProfissional(5L)).thenReturn(List.of(s));

        mockMvc.perform(get("/api/solicitacoes").param("idProfissional", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    void deveRetornarBadRequestSemParametros() throws Exception {
        mockMvc.perform(get("/api/solicitacoes"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarStatus() throws Exception {
        AtualizarStatusDTO dto = AtualizarStatusDTO.builder()
                .novoStatus(StatusSolicitacao.EM_ANDAMENTO)
                .build();

        Solicitacao atualizada = Solicitacao.builder()
                .id(1L).titulo("Teste").status(StatusSolicitacao.EM_ANDAMENTO)
                .criadoEm(LocalDateTime.now()).atualizadoEm(LocalDateTime.now()).build();

        when(solicitacaoAppService.atualizarStatus(eq(1L), any(AtualizarStatusDTO.class)))
                .thenReturn(atualizada);

        mockMvc.perform(put("/api/solicitacoes/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EM_ANDAMENTO"));
    }
}
