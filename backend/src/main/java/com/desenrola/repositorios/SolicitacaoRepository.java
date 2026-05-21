package com.desenrola.repositorios;

import com.desenrola.repositorios.entidades.Solicitacao;
import com.desenrola.repositorios.entidades.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    List<Solicitacao> findByClienteIdUsuarioOrderByCriadoEmDesc(Long idCliente);

    List<Solicitacao> findByProfissionalIdUsuarioOrderByCriadoEmDesc(Long idProfissional);

    List<Solicitacao> findByStatus(StatusSolicitacao status);
}
