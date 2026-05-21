package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CadastroClienteDTO;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.UsuarioRepository;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteAppServiceTest {

    @Mock private ClienteRepository clienteRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ClienteAppService clienteAppService;

    @Test
    void deveCadastrarClienteComSucesso() {
        CadastroClienteDTO dto = CadastroClienteDTO.builder()
                .nome("João")
                .email("joao@email.com")
                .senha("123456")
                .telefone("41999999999")
                .cpf("12345678901")
                .dataNascimento(LocalDate.of(1990, 1, 1))
                .endereco("Rua A")
                .cidade("Curitiba")
                .estado("PR")
                .cep("80000000")
                .build();

        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(clienteRepository.existsByCpf("12345678901")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> {
            Cliente c = inv.getArgument(0);
            c.setIdUsuario(1L);
            return c;
        });

        Cliente resultado = clienteAppService.cadastrar(dto);

        assertThat(resultado.getIdUsuario()).isEqualTo(1L);
        assertThat(resultado.getCpf()).isEqualTo("12345678901");
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExiste() {
        CadastroClienteDTO dto = CadastroClienteDTO.builder()
                .email("existente@email.com").build();

        when(usuarioRepository.existsByEmail("existente@email.com")).thenReturn(true);

        assertThatThrownBy(() -> clienteAppService.cadastrar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("e-mail");
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaExiste() {
        CadastroClienteDTO dto = CadastroClienteDTO.builder()
                .email("novo@email.com")
                .cpf("11111111111")
                .build();

        when(usuarioRepository.existsByEmail("novo@email.com")).thenReturn(false);
        when(clienteRepository.existsByCpf("11111111111")).thenReturn(true);

        assertThatThrownBy(() -> clienteAppService.cadastrar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CPF");
    }
}
