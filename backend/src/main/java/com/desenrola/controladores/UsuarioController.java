package com.desenrola.controladores;

import com.desenrola.controladores.dto.LoginDTO;
import com.desenrola.controladores.dto.UsuarioPerfilDTO;
import com.desenrola.repositorios.ClienteRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.UsuarioRepository;
import com.desenrola.repositorios.entidades.Cliente;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.repositorios.entidades.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ProfissionalRepository profissionalRepository;

    @PostMapping("/login")
    public ResponseEntity<UsuarioPerfilDTO> login(@RequestBody LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha invÃ¡lidos."));

        if (!Boolean.TRUE.equals(usuario.getAtivo()) || !BCrypt.checkpw(dto.getSenha(), usuario.getSenhaHash())) {
            throw new IllegalArgumentException("E-mail ou senha invÃ¡lidos.");
        }

        return ResponseEntity.ok(toPerfilDTO(usuario));
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioPerfilDTO> perfil(@RequestParam String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("UsuÃ¡rio nÃ£o encontrado."));

        return ResponseEntity.ok(toPerfilDTO(usuario));
    }

    private UsuarioPerfilDTO toPerfilDTO(Usuario usuario) {
        Cliente cliente = clienteRepository.findById(usuario.getId()).orElse(null);
        Profissional profissional = profissionalRepository.findById(usuario.getId()).orElse(null);

        String cidade = cliente != null ? cliente.getCidade() : null;
        String estado = cliente != null ? cliente.getEstado() : null;
        String endereco = cliente != null ? cliente.getEndereco() : null;

        if (cidade == null && profissional != null) {
            cidade = profissional.getCidade();
        }

        if (estado == null && profissional != null) {
            estado = profissional.getEstado();
        }

        return UsuarioPerfilDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .tipo(usuario.getTipo())
                .endereco(endereco)
                .cidade(cidade)
                .estado(estado)
                .build();
    }
}
