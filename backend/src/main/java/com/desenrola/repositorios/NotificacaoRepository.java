package com.desenrola.repositorios;

import com.desenrola.repositorios.entidades.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByIdUsuarioOrderByCriadoEmDesc(Long idUsuario);

    List<Notificacao> findByIdUsuarioAndLidaFalseOrderByCriadoEmDesc(Long idUsuario);
}
