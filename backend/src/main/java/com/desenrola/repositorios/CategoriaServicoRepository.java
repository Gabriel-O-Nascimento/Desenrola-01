package com.desenrola.repositorios;

import com.desenrola.repositorios.entidades.CategoriaServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaServicoRepository extends JpaRepository<CategoriaServico, Integer> {

    List<CategoriaServico> findByAtivoTrue();
}
