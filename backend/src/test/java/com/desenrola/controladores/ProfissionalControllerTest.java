package com.desenrola.controladores;

import com.desenrola.controladores.dto.CadastroProfissionalDTO;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.servicos.aplicacao.ProfissionalAppService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfissionalController.class)
class ProfissionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProfissionalAppService profissionalAppService;

    @MockitoBean
    private ProfissionalRepository profissionalRepository;

    @Test
    void deveCadastrarProfissionalComStatus201() throws Exception {
        CadastroProfissionalDTO dto = CadastroProfissionalDTO.builder()
                .nome("Maria Eletricista")
                .email("maria@email.com")
                .senha("123456")
                .telefone("41988888888")
                .documento("12345678901234")
                .tipoDocumento(Profissional.TipoDocumento.CNPJ)
                .idCategoria(1)
                .especialidade("Elétrica residencial")
                .descricaoPerfil("10 anos de experiência")
                .cidade("Curitiba")
                .estado("PR")
                .raioAtendimentoKm(20)
                .build();

        Profissional profissional = Profissional.builder()
                .idUsuario(2L)
                .especialidade("Elétrica residencial")
                .disponivel(true)
                .avaliacaoMedia(BigDecimal.ZERO)
                .totalAvaliacoes(0)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(profissionalAppService.cadastrar(any(CadastroProfissionalDTO.class))).thenReturn(profissional);

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(2))
                .andExpect(jsonPath("$.especialidade").value("Elétrica residencial"));
    }

    @Test
    void deveListarProfissionaisDisponiveis() throws Exception {
        Profissional p = Profissional.builder()
                .idUsuario(3L)
                .disponivel(true)
                .avaliacaoMedia(new BigDecimal("4.50"))
                .totalAvaliacoes(10)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(profissionalRepository.findByDisponivelTrue()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(3));
    }

    @Test
    void deveListarProfissionaisPorCategoria() throws Exception {
        Profissional p = Profissional.builder()
                .idUsuario(4L)
                .disponivel(true)
                .avaliacaoMedia(BigDecimal.ZERO)
                .totalAvaliacoes(0)
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(profissionalRepository.findByDisponivelTrueAndCategoriaId(2)).thenReturn(List.of(p));

        mockMvc.perform(get("/api/profissionais").param("idCategoria", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idUsuario").value(4));
    }
}
