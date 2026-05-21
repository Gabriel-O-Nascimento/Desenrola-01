package com.desenrola.controladores;

import com.desenrola.controladores.dto.CriarAvaliacaoDTO;
import com.desenrola.repositorios.AvaliacaoRepository;
import com.desenrola.repositorios.entidades.Avaliacao;
import com.desenrola.servicos.aplicacao.AvaliacaoAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoAppService avaliacaoAppService;
    private final AvaliacaoRepository avaliacaoRepository;

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@RequestBody CriarAvaliacaoDTO dto) {
        Avaliacao avaliacao = avaliacaoAppService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @GetMapping("/profissional/{idProfissional}")
    public ResponseEntity<List<Avaliacao>> listarPorProfissional(@PathVariable Long idProfissional) {
        return ResponseEntity.ok(
                avaliacaoRepository.findByProfissionalIdUsuarioOrderByCriadoEmDesc(idProfissional)
        );
    }
}
