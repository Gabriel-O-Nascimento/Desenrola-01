package com.desenrola.servicos.aplicacao;

import com.desenrola.controladores.dto.CadastroProfissionalDTO;
import com.desenrola.repositorios.CategoriaServicoRepository;
import com.desenrola.repositorios.ProfissionalRepository;
import com.desenrola.repositorios.UsuarioRepository;
import com.desenrola.repositorios.entidades.CategoriaServico;
import com.desenrola.repositorios.entidades.Profissional;
import com.desenrola.repositorios.entidades.TipoUsuario;
import com.desenrola.repositorios.entidades.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfissionalAppService {

    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaServicoRepository categoriaServicoRepository;

    @Transactional
    public Profissional cadastrar(CadastroProfissionalDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse e-mail.");
        }

        CategoriaServico categoria = null;
        if (dto.getIdCategoria() != null) {
            categoria = categoriaServicoRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
        }

        LocalDateTime agora = LocalDateTime.now();

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senhaHash(BCrypt.hashpw(dto.getSenha(), BCrypt.gensalt()))
                .telefone(dto.getTelefone())
                .tipo(TipoUsuario.PROFISSIONAL)
                .ativo(true)
                .emailVerificado(false)
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Profissional profissional = Profissional.builder()
                .usuario(usuarioSalvo)
                .documento(dto.getDocumento())
                .tipoDocumento(dto.getTipoDocumento())
                .categoria(categoria)
                .especialidade(dto.getEspecialidade())
                .descricaoPerfil(dto.getDescricaoPerfil())
                .avaliacaoMedia(BigDecimal.ZERO)
                .totalAvaliacoes(0)
                .disponivel(true)
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .raioAtendimentoKm(dto.getRaioAtendimentoKm())
                .criadoEm(agora)
                .atualizadoEm(agora)
                .build();

        return profissionalRepository.save(profissional);
    }
}
