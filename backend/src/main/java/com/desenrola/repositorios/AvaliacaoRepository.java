package com.desenrola.repositorios;

import com.desenrola.repositorios.entidades.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    Optional<Avaliacao> findBySolicitacaoId(Long idSolicitacao);

    List<Avaliacao> findByProfissionalIdUsuarioOrderByCriadoEmDesc(Long idProfissional);
}
