package com.desenrola.servicos;

import com.desenrola.dto.CriarProfissionalRequest;
import com.desenrola.dto.ProfissionalResponse;
import com.desenrola.modelos.Profissional;
import com.desenrola.modelos.enums.TipoUsuario;
import com.desenrola.repositorios.ProfissionalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {

    private static final String SENHA_PADRAO = "senha-temporaria";

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public ProfissionalResponse criar(CriarProfissionalRequest request) {
        Profissional profissional = new Profissional();
        profissional.setNome(request.nome());
        profissional.setEmail(request.email());
        profissional.setTelefone(request.telefone());
        profissional.setSenhaHash(SENHA_PADRAO);
        profissional.setTipo(TipoUsuario.PROFISSIONAL);
        profissional.setEspecialidade(request.especialidade());
        profissional.setDisponivel(request.disponivel());
        profissional.setAvaliacaoMedia(0.0);
        profissional.setTotalAvaliacoes(0);
        return toResponse(profissionalRepository.save(profissional));
    }

    public List<ProfissionalResponse> listar(String especialidade, Boolean disponivel) {
        List<Profissional> profissionais;

        if (especialidade != null && !especialidade.isBlank() && disponivel != null) {
            profissionais = profissionalRepository.findByEspecialidadeContainingIgnoreCaseAndDisponivel(especialidade, disponivel);
        } else if (especialidade != null && !especialidade.isBlank()) {
            profissionais = profissionalRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
        } else if (disponivel != null) {
            profissionais = profissionalRepository.findByDisponivel(disponivel);
        } else {
            profissionais = profissionalRepository.findAll();
        }

        return profissionais.stream().map(this::toResponse).toList();
    }

    public Profissional buscarEntidade(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profissional nao encontrado."));
    }

    public ProfissionalResponse buscarPorId(Long id) {
        return toResponse(buscarEntidade(id));
    }

    public void atualizarAvaliacaoMedia(Profissional profissional, double media) {
        profissional.setAvaliacaoMedia(media);
        profissionalRepository.save(profissional);
    }

    public void atualizarDisponibilidade(Profissional profissional, boolean disponivel) {
        profissional.setDisponivel(disponivel);
        profissionalRepository.save(profissional);
    }

    public ProfissionalResponse toResponse(Profissional profissional) {
        return new ProfissionalResponse(
                profissional.getId(),
                profissional.getNome(),
                profissional.getEmail(),
                profissional.getTelefone(),
                profissional.getEspecialidade(),
                profissional.getDisponivel(),
                profissional.getAvaliacaoMedia()
        );
    }
}
