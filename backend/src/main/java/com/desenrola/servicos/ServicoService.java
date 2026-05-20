package com.desenrola.servicos;

import com.desenrola.dto.ServicoResponse;
import com.desenrola.modelos.Servico;
import com.desenrola.repositorios.ServicoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    public List<ServicoResponse> listar() {
        return servicoRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Servico buscarEntidade(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Servico nao encontrado."));
    }

    private ServicoResponse toResponse(Servico servico) {
        return new ServicoResponse(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getCategoria(),
                servico.getPrecoBase()
        );
    }
}
