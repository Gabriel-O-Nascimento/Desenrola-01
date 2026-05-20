package com.desenrola.repositorios;

import com.desenrola.modelos.Avaliacao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    @EntityGraph(attributePaths = {"cliente", "profissional", "solicitacao"})
    List<Avaliacao> findByProfissionalId(Long profissionalId);
}
