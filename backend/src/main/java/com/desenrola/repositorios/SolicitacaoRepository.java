package com.desenrola.repositorios;

import com.desenrola.modelos.Solicitacao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    @Override
    @EntityGraph(attributePaths = {"cliente", "profissional", "servico"})
    List<Solicitacao> findAll();

    @Override
    @EntityGraph(attributePaths = {"cliente", "profissional", "servico"})
    Optional<Solicitacao> findById(Long id);
}
