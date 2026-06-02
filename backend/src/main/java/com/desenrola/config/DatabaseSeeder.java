package com.desenrola.config;

import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.UsuarioRepository;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.TipoUsuario;
import com.desenrola.repositorios.entidades.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private static final String DEFAULT_EMAIL = "joao@email.com";

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByEmail(DEFAULT_EMAIL)) {
            return;
        }

        LocalDateTime agora = LocalDateTime.now();

        Usuario usuario = Usuario.builder()
                .nome("Joao da Silva")
                .email(DEFAULT_EMAIL)
                .senhaHash(BCrypt.hashpw("Senha123", BCrypt.gensalt()))
                .telefone("11987654321")
                .tipo(TipoUsuario.CLIENTE)
                .ativo(true)
                .emailVerificado(true)
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Cliente cliente = Cliente.builder()
                .usuario(usuarioSalvo)
                .cpf("12345678909")
                .endereco("Rua das Flores, 123")
                .cidade("Sao Paulo")
                .estado("SP")
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        clienteRepository.save(cliente);
    }
}
