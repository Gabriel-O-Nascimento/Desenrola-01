package com.desenrola.controladores;

import com.desenrola.repositorios.CategoriaServicoRepository;
import com.desenrola.repositorios.ServicoRepository;
import com.desenrola.repositorios.entidades.CategoriaServico;
import com.desenrola.repositorios.entidades.Servico;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaServicoRepository categoriaRepository;
    private final ServicoRepository servicoRepository;

    @GetMapping
    public ResponseEntity<List<CategoriaServico>> listar() {
        return ResponseEntity.ok(categoriaRepository.findByAtivoTrue());
    }

    @GetMapping("/{id}/servicos")
    public ResponseEntity<List<Servico>> listarServicosDaCategoria(@PathVariable Integer id) {
        return ResponseEntity.ok(servicoRepository.findByAtivoTrueAndCategoriaId(id));
    }
}
