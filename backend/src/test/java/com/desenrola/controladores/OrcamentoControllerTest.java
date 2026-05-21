package com.desenrola.controladores;

import com.desenrola.controladores.dto.CriarOrcamentoDTO;
import com.desenrola.repositorios.entidades.Orcamento;
import com.desenrola.repositorios.entidades.StatusOrcamento;
import com.desenrola.servicos.aplicacao.OrcamentoAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrcamentoController.class)
class OrcamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrcamentoAppService orcamentoAppService;

    @Test
    void deveCriarOrcamentoComStatus201() throws Exception {
        CriarOrcamentoDTO dto = CriarOrcamentoDTO.builder()
                .idSolicitacao(1L)
                .idProfissional(5L)
                .observacoes("Inclui material")
                .totalMaoObra(new BigDecimal("150.00"))
                .totalMateriais(new BigDecimal("80.00"))
                .build();

        Orcamento orcamento = Orcamento.builder()
                .id(1L)
                .totalMaoObra(new BigDecimal("150.00"))
                .totalMateriais(new BigDecimal("80.00"))
                .totalGeral(new BigDecimal("230.00"))
                .status(StatusOrcamento.PENDENTE)
                .observacoes("Inclui material")
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(orcamentoAppService.criar(any(CriarOrcamentoDTO.class))).thenReturn(orcamento);

        mockMvc.perform(post("/api/orcamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.totalGeral").value(230.00))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }
}
