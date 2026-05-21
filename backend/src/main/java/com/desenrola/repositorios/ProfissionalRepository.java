package com.desenrola.repositorios;

import com.desenrola.repositorios.entidades.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByDisponivelTrueAndCategoriaId(Integer categoriaId);

    List<Profissional> findByDisponivelTrue();
}
