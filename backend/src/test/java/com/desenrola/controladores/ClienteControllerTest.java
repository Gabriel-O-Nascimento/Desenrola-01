package com.desenrola.controladores;

import com.desenrola.controladores.dto.CadastroClienteDTO;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.servicos.aplicacao.ClienteAppService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteAppService clienteAppService;

    @Test
    void deveCadastrarClienteComStatus201() throws Exception {
        CadastroClienteDTO dto = CadastroClienteDTO.builder()
                .nome("João Silva")
                .email("joao@email.com")
                .senha("123456")
                .telefone("41999999999")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 5, 15))
                .endereco("Rua A, 50")
                .cidade("Curitiba")
                .estado("PR")
                .cep("80000000")
                .build();

        Cliente cliente = Cliente.builder()
                .idUsuario(1L)
                .cpf("12345678901")
                .cidade("Curitiba")
                .estado("PR")
                .criadoEm(LocalDateTime.now())
                .atualizadoEm(LocalDateTime.now())
                .build();

        when(clienteAppService.cadastrar(any(CadastroClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.cpf").value("12345678901"));
    }
}
