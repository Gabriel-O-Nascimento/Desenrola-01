package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CadastroClienteDTO;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.UsuarioRepository;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.TipoUsuario;
import com.desenrola.repositorios.entidades.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClienteAppService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Cliente cadastrar(CadastroClienteDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse e-mail.");
        }
        if (dto.getCpf() != null && clienteRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com esse CPF.");
        }

        LocalDateTime agora = LocalDateTime.now();

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senhaHash(BCrypt.hashpw(dto.getSenha(), BCrypt.gensalt()))
                .telefone(dto.getTelefone())
                .tipo(TipoUsuario.CLIENTE)
                .ativo(true)
                .emailVerificado(false)
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Cliente cliente = Cliente.builder()
                .usuario(usuarioSalvo)
                .cpf(dto.getCpf())
                .dataNascimento(dto.getDataNascimento())
                .endereco(dto.getEndereco())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        return clienteRepository.save(cliente);
    }
}
