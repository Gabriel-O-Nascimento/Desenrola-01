package com.desenrola.controladores;

import com.desenrola.controladores.dto.CriarAvaliacaoDTO;
import com.desenrola.repositorios.AvaliacaoRepository;
import com.desenrola.repositorios.entidades.Avaliacao;
import com.desenrola.servicos.aplicacao.AvaliacaoAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvaliacaoController.class)
class AvaliacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AvaliacaoAppService avaliacaoAppService;

    @MockitoBean
    private AvaliacaoRepository avaliacaoRepository;

    @Test
    void deveCriarAvaliacaoComStatus201() throws Exception {
        CriarAvaliacaoDTO dto = CriarAvaliacaoDTO.builder()
                .idSolicitacao(1L)
                .idCliente(10L)
                .idProfissional(5L)
                .nota(5)
                .comentario("Excelente trabalho!")
                .build();

        Avaliacao avaliacao = Avaliacao.builder()
                .id(1L)
                .nota(5)
                .comentario("Excelente trabalho!")
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(avaliacaoAppService.criar(any(CriarAvaliacaoDTO.class))).thenReturn(avaliacao);

        mockMvc.perform(post("/api/avaliacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nota").value(5))
                .andExpect(jsonPath("$.comentario").value("Excelente trabalho!"));
    }

    @Test
    void deveListarAvaliacoesPorProfissional() throws Exception {
        Avaliacao a = Avaliacao.builder()
                .id(2L)
                .nota(4)
                .comentario("Bom serviço")
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(avaliacaoRepository.findByProfissionalIdUsuarioOrderByCriadoEmDesc(5L))
                .thenReturn(List.of(a));

        mockMvc.perform(get("/api/avaliacoes/profissional/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].nota").value(4));
    }
}
