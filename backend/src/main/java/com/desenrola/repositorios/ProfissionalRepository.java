package com.desenrola.repositorios;

import com.desenrola.modelos.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    List<Profissional> findByDisponivel(Boolean disponivel);

    List<Profissional> findByEspecialidadeContainingIgnoreCase(String especialidade);

    List<Profissional> findByEspecialidadeContainingIgnoreCaseAndDisponivel(String especialidade, Boolean disponivel);
}
